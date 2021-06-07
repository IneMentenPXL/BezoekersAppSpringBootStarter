package be.pxl.ja2.bezoekersapp.rest;

import be.pxl.ja2.bezoekersapp.rest.resources.RegistreerBezoekerResource;
import be.pxl.ja2.bezoekersapp.service.BezoekersService;
import be.pxl.ja2.bezoekersapp.util.exception.BezoekersAppException;
import be.pxl.ja2.bezoekersapp.util.exception.OngeldigTijdstipException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BezoekersRest.class)
public class BezoekersRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BezoekersService bezoekersService;

    @Captor
    private ArgumentCaptor<RegistreerBezoekerResource> registreerBezoekerResourceArgumentCaptor;

    @Test
    public void badRequestWhenPhonenumberNotGiven() throws Exception {
        mockMvc.perform(post("/bezoekers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"patientCode\": \"P001\", \"tijdstip\": \"16:40\", \"naam\": \"Overschots\", \"voornaam\": \"Max\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnsVisitorIdWhenRequestValid() throws Exception {
        Mockito.when(bezoekersService.registreerBezoeker(Mockito.any())).thenReturn(112L);

        mockMvc.perform(post("/bezoekers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"patientCode\": \"P001\", \"tijdstip\": \"16:40\", \"naam\": \"Overschots\", \"voornaam\": \"Max\", \"telefoonnummer\": \"0470011003\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("112"));

        Mockito.verify(bezoekersService).registreerBezoeker(registreerBezoekerResourceArgumentCaptor.capture());

        RegistreerBezoekerResource resource = registreerBezoekerResourceArgumentCaptor.getValue();

        Assertions.assertEquals("Max", resource.getVoornaam());
        Assertions.assertEquals("P001", resource.getPatientCode());
        Assertions.assertEquals(LocalTime.parse("16:40"), resource.getTijdstip());
        Assertions.assertEquals("Overschots", resource.getNaam());
        Assertions.assertEquals("0470011003", resource.getTelefoonnummer());

    }
}
