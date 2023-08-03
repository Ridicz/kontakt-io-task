package io.kontak.apps.api;

import io.kontak.apps.storage.AnomalyRepository;
import io.kontak.apps.storage.entity.AnomalyEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Testcontainers
@SpringBootTest(classes = TemperatureAnalyticsApiApplication.class)
@AutoConfigureMockMvc
public class AnomalyControllerTest {

    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnomalyRepository anomalyRepository;

    @BeforeEach
    void setUp() {
        this.anomalyRepository.saveAll(createAnomalies());
    }

    @AfterEach
    void cleanUp() {
        this.anomalyRepository.deleteAll();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        mongoDBContainer.start();
    }

    @Test
    public void testRetrievingForThermometerId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/thermometers/{thermometerID}/anomalies", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].thermometerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].thermometerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].thermometerId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[3].thermometerId").value(1));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/thermometers/{thermometerID}/anomalies", "2"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].thermometerId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].thermometerId").value(2));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/thermometers/{thermometerID}/anomalies", "3"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].thermometerId").value(3));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/thermometers/{thermometerID}/anomalies", "4"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].thermometerId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].thermometerId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].thermometerId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[3].thermometerId").value(4));
    }

    @Test
    public void testRetrievingForRoomId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomsID}/anomalies", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roomId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].roomId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].roomId").value(1));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomsID}/anomalies", "2"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roomId").value(2));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomsID}/anomalies", "3"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roomId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].roomId").value(3));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomsID}/anomalies", "4"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roomId").value(4));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomsID}/anomalies", "5"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roomId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].roomId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].roomId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[3].roomId").value(5));
    }

    @Test
    public void testRetrievingThermometersWithMoreAnomaliesThan() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/thermometers/threshold?threshold={threshold}", "3"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(content().string(containsString("\"thermometerId\":\"1\"")))
                .andExpect(content().string(containsString("\"thermometerId\":\"4\"")));
    }

    private List<AnomalyEntity> createAnomalies() {
        return List.of(
                AnomalyEntity.builder().temperature(21.30).thermometerId("1").roomId("1").timestamp(Instant.now()).build(),
                AnomalyEntity.builder().temperature(21.31).thermometerId("1").roomId("2").timestamp(Instant.now().plusMillis(10)).build(),
                AnomalyEntity.builder().temperature(21.32).thermometerId("1").roomId("1").timestamp(Instant.now().plusMillis(20)).build(),
                AnomalyEntity.builder().temperature(21.32).thermometerId("1").roomId("1").timestamp(Instant.now().plusMillis(30)).build(),
                AnomalyEntity.builder().temperature(21.33).thermometerId("2").roomId("3").timestamp(Instant.now().plusMillis(40)).build(),
                AnomalyEntity.builder().temperature(21.34).thermometerId("2").roomId("3").timestamp(Instant.now().plusMillis(50)).build(),
                AnomalyEntity.builder().temperature(21.35).thermometerId("3").roomId("4").timestamp(Instant.now().plusMillis(60)).build(),
                AnomalyEntity.builder().temperature(22.35).thermometerId("4").roomId("5").timestamp(Instant.now().plusMillis(70)).build(),
                AnomalyEntity.builder().temperature(23.35).thermometerId("4").roomId("5").timestamp(Instant.now().plusMillis(80)).build(),
                AnomalyEntity.builder().temperature(24.35).thermometerId("4").roomId("5").timestamp(Instant.now().plusMillis(90)).build(),
                AnomalyEntity.builder().temperature(25.35).thermometerId("4").roomId("5").timestamp(Instant.now().plusMillis(100)).build()
        );
    }
}
