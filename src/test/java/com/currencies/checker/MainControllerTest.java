package com.currencies.checker;

import com.currencies.checker.controllers.MainController;
import com.currencies.checker.services.GIFService;
import com.currencies.checker.services.RateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTest {

    @MockBean
    private RateService rateService;
    @MockBean
    private GIFService gifService;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Value("${giphy.rich}")
    private String richStatus;
    @Value("${giphy.broke}")
    private String brokeStatus;
    @Value("${giphy.idk}")
    private String idkStatus;
    @Value("${giphy.equals}")
    private String equalsStatus;


    @Test
    public void whatIfReturnListOfCharCodes() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("TEST");

        Mockito.when(rateService.getCurrenciesCodes())
                .thenReturn(list);

        mockMvc.perform(get("/exchange/currencies")
                        .content(mapper.writeValueAsString(list))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$[0]").value("TEST"));
    }

    @Test
    public void whatIfListIsNull() throws Exception {
        Mockito.when(rateService.getCurrenciesCodes())
                .thenReturn(null);

        mockMvc.perform(get("/exchange/currencies")
                        .content(mapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void whatIfReturnRichGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.richStatus);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);

        Mockito.when(rateService.comparisonStatus(anyString()))
                .thenReturn(1);

        Mockito.when(gifService.getGif(this.richStatus))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/exchange/reaction/TEST-CODE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.richStatus));
    }

    @Test
    public void whatIfReturnBrokeGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.brokeStatus);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);

        Mockito.when(rateService.comparisonStatus(anyString()))
                .thenReturn(-1);

        Mockito.when(gifService.getGif(this.brokeStatus))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/exchange/reaction/TEST-CODE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.brokeStatus));
    }

    @Test
    public void whatIfReturnEqualsGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.equalsStatus);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);

        Mockito.when(rateService.comparisonStatus(anyString()))
                .thenReturn(0);

        Mockito.when(gifService.getGif(this.equalsStatus))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/exchange/reaction/TEST-CODE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.equalsStatus));
    }

    @Test
    public void whatIfReturnErrorGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.idkStatus);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);

        Mockito.when(rateService.comparisonStatus(anyString()))
                .thenReturn(101);

        Mockito.when(gifService.getGif(this.idkStatus))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/exchange/reaction/TEST-CODE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.idkStatus));
    }


}