package com.shop.book.api.book.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.book.api.book.service.BookService;
import com.shop.book.domain.book.dto.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class BookServiceImpl implements BookService {

    @Value("${naver.openapi.clientId}")
    private String clientId;

    @Value("${naver.openapi.clientSecret}")
    private String clientSecret;

    private static final String NAVER_BOOK_API_URL = "https://openapi.naver.com/v1/search/book.json";

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    @Override
    public List<BookDto> searchBooks(String query, Integer display) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String apiURL = NAVER_BOOK_API_URL + "?query=" + encodedQuery + "&display=" + display;
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);

        List<BookDto> books = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            NaverBookResponse bookResponse = mapper.readValue(responseBody, NaverBookResponse.class);

            if (bookResponse != null && bookResponse.getItems() != null && !bookResponse.getItems().isEmpty()) {
                books = bookResponse.getItems().stream().map(this::convertToBookDto)
                        .collect(Collectors.toList());
                logger.info("검색 결과: {} 건", books.size());
            } else {
                logger.info("검색 결과 없음: {}", query);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    @Override
    public BookSearchXmlResponse.Item getBookDetailByIsbn(String isbn) {
        System.out.println("Fetching book details for ISBN: " + isbn);
        String url = "https://openapi.naver.com/v1/search/book_adv.xml?d_isbn=" + isbn;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String xmlResponse = response.getBody();

                JAXBContext jaxbContext = JAXBContext.newInstance(BookSearchXmlResponse.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                StringReader reader = new StringReader(xmlResponse);

                BookSearchXmlResponse bookSearchXmlResponse = (BookSearchXmlResponse) unmarshaller.unmarshal(reader);

                if (!bookSearchXmlResponse.getChannel().getItems().isEmpty()) {
                    BookSearchXmlResponse.Item item = bookSearchXmlResponse.getChannel().getItems().get(0);

                    return item;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private BookDto convertToBookDto(NaverBookItem naverBookItem) {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(naverBookItem.getTitle() != null ? naverBookItem.getTitle() : "");
        bookDto.setTitle(naverBookItem.getTitle() != null ? naverBookItem.getTitle() : "");
        bookDto.setAuthor(naverBookItem.getAuthor() != null ? naverBookItem.getAuthor() : "");
        bookDto.setPublisher(naverBookItem.getPublisher() != null ? naverBookItem.getPublisher() : "");
        bookDto.setPubdate(naverBookItem.getPubdate() != null ? naverBookItem.getPubdate() : "");
        bookDto.setDescription(naverBookItem.getDescription());
        bookDto.setIsbn(naverBookItem.getIsbn() != null ? naverBookItem.getIsbn() : "");
        bookDto.setImage(naverBookItem.getImage() != null ? naverBookItem.getImage() : "");
        bookDto.setLink(naverBookItem.getLink() != null ? naverBookItem.getLink() : "");
        bookDto.setDiscount(naverBookItem.getDiscount() != null ? naverBookItem.getDiscount() : "");
        return bookDto;
    }


    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

        private static HttpURLConnection connect(String apiUrl){
            try {
                URL url = new URL(apiUrl);
                return (HttpURLConnection)url.openConnection();
            } catch (MalformedURLException e) {
                throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
            } catch (IOException e) {
                throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
            }
        }
    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }
}
