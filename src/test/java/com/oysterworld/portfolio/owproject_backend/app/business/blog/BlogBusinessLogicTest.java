package com.oysterworld.portfolio.owproject_backend.app.business.blog;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.stream.IntStream;

import com.oysterworld.portfolio.owproject_backend.common.IdHashUtils;
import com.oysterworld.portfolio.owproject_backend.database.mapper.BlogMapper;
import com.oysterworld.portfolio.owproject_backend.exception.OwBadRequestException;
import com.oysterworld.portfolio.owproject_backend.exception.OwInternalServerErrorException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = com.oysterworld.portfolio.owproject_backend.app.OwprojectBackendApplication.class)
@Transactional
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.oysterworld.portfolio.owproject_backend")
public class BlogBusinessLogicTest {
    @Autowired
    public BlogBusinessLogic blogBusinessLogic;

    @Autowired
    public BlogMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testGetContentByTitleNoResult() {
        var result = blogBusinessLogic.getContentByTitle("title");
        assertEquals(0, result.size());
    }

    @Test
    void testGetContentByTitleHasManyResult() {
        addTestData(3);
        var result = blogBusinessLogic.getContentByTitle("title");
        assertEquals(3, result.size());
        IntStream.range(0, 3).forEach((n) -> {
            var number = n + 1;
            assertEquals("test-id-" + number, result.get(n).getId());
            assertEquals("test-title-" + number, result.get(n).getTitle());
            assertEquals("test-content-" + number, result.get(n).getContent());
        });
    }

    @Test
    void testGetContentByTitleErrorTitleBlank() {
        try {
            blogBusinessLogic.getContentByTitle("");
            fail();
        } catch (OwBadRequestException e) {
            assertEquals("input parameter is empty. Input Value : \"\"", e.getMessage());
        }
    }

    @Test
    void testGetContentByTitleErrorTitleAllSpace() {
        try {
            blogBusinessLogic.getContentByTitle("        ");
            fail();
        } catch (OwBadRequestException e) {
            assertEquals("input parameter is empty. Input Value : \"        \"", e.getMessage());
        }
    }

    @Test
    void testGetContentByIdNoResult() {
        var result = blogBusinessLogic.getContentById("id");
        assertNull(result.getId());
        assertNull(result.getTitle());
        assertNull(result.getContent());
    }

    @Test
    void testGetContentByIdHasResult() {
        addTestData(3);
        var result = blogBusinessLogic.getContentById("id");
        assertNull(result.getId());
        assertNull(result.getTitle());
        assertNull(result.getContent());
        result = blogBusinessLogic.getContentById("test-id-1");
        assertEquals("test-id-1", result.getId());
        assertEquals("test-title-1", result.getTitle());
        assertEquals("test-content-1", result.getContent());
    }

    @Test
    void testGetContentByIdErrorTitleBlank() {
        try {
            blogBusinessLogic.getContentById("");
            fail();
        } catch (OwBadRequestException e) {
            assertEquals("input parameter is empty. Input Value : \"\"", e.getMessage());
        }
    }

    @Test
    void testGetContentByIdErrorTitleAllSpace() {
        try {
            blogBusinessLogic.getContentById("        ");
            fail();
        } catch (OwBadRequestException e) {
            assertEquals("input parameter is empty. Input Value : \"        \"", e.getMessage());
        }
    }

    @Test
    void testGetAllContentHasNoResult() {
        var result = blogBusinessLogic.getAllContents();
        assertEquals(0, result.size());
    }

    @Test
    void testGetAllContentHasManyResult() {
        addTestData(10);
        var result = blogBusinessLogic.getAllContents();
        assertEquals(10, result.size());
        IntStream.range(0, 10).forEach((n) -> {
            var number = n + 1;
            assertEquals("test-id-" + number, result.get(n).getId());
            assertEquals("test-title-" + number, result.get(n).getTitle());
            assertEquals("test-content-" + number, result.get(n).getContent());
        });
    }

    @Test
    void testPostContentHasOneResult() {
        var blog = new Blog(null, "test-title", "test-content", null);
        var result = blogBusinessLogic.postContent(blog.toRequest());
        assertTrue(result.getId().matches("^[a-f0-9]{64}$"));
        assertEquals("test-title", result.getTitle());
        assertEquals("test-content", result.getContent());
        var dbResult = jdbcTemplate.queryForList("SELECT * FROM BLOG");
        assertEquals(1, dbResult.size());
        assertEquals(1, dbResult.get(0).get("inner_id"));
    }

    @Test
    void testPostContentHasManyResult() {
        var blog1 = new Blog(null, "test-title-1", "test-content", null);
        var blog2 = new Blog(null, "test-title-2", "test-content", null);
        blogBusinessLogic.postContent(blog1.toRequest());
        blogBusinessLogic.postContent(blog2.toRequest());
        var dbResult = jdbcTemplate.queryForList("SELECT * FROM BLOG");
        assertEquals(2, dbResult.size());
        assertEquals(1, dbResult.get(0).get("inner_id"));
        assertEquals(2, dbResult.get(1).get("inner_id"));
    }

    @Test
    void testPostContentErrorTitleAlreadyExists() {
        var blog1 = new Blog(null, "test-title", "test-content-1", null);
        var blog2 = new Blog(null, "test-title", "test-content-2", null);
        try {
            blogBusinessLogic.postContent(blog1.toRequest());
            blogBusinessLogic.postContent(blog2.toRequest());
            fail();
        } catch (OwBadRequestException e) {
            assertEquals("input title already exists. Input Value : \"test-title\"", e.getMessage());
        }
    }

    @Test
    void testPostContentErrorDuplicateId() {
        try (MockedStatic<IdHashUtils> mockedStatic = mockStatic(IdHashUtils.class)) {
            String fixedHashedId = "test-hash-id";
            mockedStatic.when(() -> IdHashUtils.hashId(anyString())).thenReturn(fixedHashedId);
            var blog1 = new Blog(null, "test-title-1", "test-content-1", null);
            var blog2 = new Blog(null, "test-title-2", "test-content-2", null);
            blogBusinessLogic.postContent(blog1.toRequest());
            blogBusinessLogic.postContent(blog2.toRequest());
            fail();
        } catch (OwInternalServerErrorException e) {
            assertEquals("Duplicate id detected.", e.getMessage());
        }
    }

    private void addTestData(int count) {
        IntStream.range(0, count).forEach((n) -> {
            var blog = new Blog("test-id-" + (n + 1), "test-title-" + (n + 1), "test-content-" + (n + 1),
                    String.valueOf(n + 1)).toModel();
            mapper.postContent(blog);
        });
    }
}
