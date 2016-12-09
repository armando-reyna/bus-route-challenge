package com.armando.bus.control;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shoulGetAJSONResponse() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set("dep_sid", "3");
        params.set("arr_sid", "4");

        this.mockMvc.perform(get("/api/direct")
                .params(params))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldIgnoreTheProcess() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set("dep_sid", "1");
        params.set("arr_sid", "1");

        this.mockMvc.perform(get("/api/direct")
                .params(params))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.dep_sid").value(0))
                .andExpect(jsonPath("$.arr_sid").value(0))
                .andExpect(jsonPath("$.direct_bus_route").value(Boolean.FALSE));
    }

    //This test will only pass if a 3 4 route exists
    @Test
    public void shouldTrueARealRoute() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set("dep_sid", "3");
        params.set("arr_sid", "4");

        this.mockMvc.perform(get("/api/direct")
                .params(params))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.dep_sid").value(3))
                .andExpect(jsonPath("$.arr_sid").value(4))
                .andExpect(jsonPath("$.direct_bus_route").value(Boolean.TRUE));
    }

    //This test will only pass if a 3 4 route doesn't exists
    @Test
    public void shouldFalseARealRoute() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set("dep_sid", "3");
        params.set("arr_sid", "5");

        this.mockMvc.perform(get("/api/direct")
                .params(params))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.dep_sid").value(0))
                .andExpect(jsonPath("$.arr_sid").value(0))
                .andExpect(jsonPath("$.direct_bus_route").value(Boolean.FALSE));
    }

}
