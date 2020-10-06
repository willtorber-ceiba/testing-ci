package com.ceiba.tiendatecnologica.infraestructura;

import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.excepcion.GarantiaExtendidaAsignadaException;
import com.ceiba.tiendatecnologica.dominio.excepcion.GarantiaExtendidaException;
import com.ceiba.tiendatecnologica.dominio.excepcion.ResourceNoFoundException;
import com.ceiba.tiendatecnologica.infraestructura.error.Error;
import com.ceiba.tiendatecnologica.util.DateTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Calendar;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ControladorGarantiaTest {

    public static String ENDPOINT_CONSULTAR_GARANTIA = "/garantia/{id}";
    public static String ENDPOINT_GENERAR_GARANTIA = "/garantia/{idProducto}/{nombreCliente}";
    public static String CONTENT_TYPE_APPLICATION_JSON_CHARSET = "application/json;charset=UTF-8";

    public static String CLIENTE_GARANTIA = "William Torres";
    public static double PRECIO_GARANTIA_MENOR = 20000d;
    public static double PRECIO_GARANTIA_MAYOR = 260000d;
    public static String CODIGO_DE_PRODUCTO_INEXISTENTE = "F10DA0E1";
    public static String CODIGO_DE_PRODUCTO_CON_GARANTIA = "PROD_001";
    public static String CODIGO_PRODUCTO_CON_PRECIO_MENOR = "PROD_003";
    public static String CODIGO_PRODUCTO_CON_PRECIO_MAYOR = "PROD_AEI";
    public static String CODIGO_PRODUCTO_SIN_GARANTIA_DISPONIBLE = "F1O8A06I";

    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya tiene asignada una garantía extendida";
    public static final String EL_PRODUCTO_NO_CUENTA_CON_GARANTIA = "Este producto no cuenta con garantía extendida";
    public static final String PRODUCTO_NO_ENCONTRADO = "No se encontró un producto. Por favor verifique el código ingresado";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Date TODAY;

    @BeforeClass
    public static void setUp() {
        TODAY = new Date();
    }

    /**
     * Validar producto inexistente
     */
    @Test
    public void generarGarantiaDeProductoInexistenteTest() throws Exception {

        String exceptionName = ResourceNoFoundException.class.getSimpleName();
        Error error = new Error(exceptionName, PRODUCTO_NO_ENCONTRADO);
        String errorResponse = objectMapper.writeValueAsString(error);

        mvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT_GENERAR_GARANTIA, CODIGO_DE_PRODUCTO_INEXISTENTE, CLIENTE_GARANTIA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_APPLICATION_JSON_CHARSET))
                .andExpect(content().json(errorResponse));
    }

    /**
     * Validar producto sin disponibilidad de garantia extendida
     */
    @Test
    public void generarGarantiaDeProductoSinGarantiaDisponibleTest() throws Exception {

        String exceptionName = GarantiaExtendidaException.class.getSimpleName();
        Error error = new Error(exceptionName, EL_PRODUCTO_NO_CUENTA_CON_GARANTIA);
        String errorResponse = objectMapper.writeValueAsString(error);

        mvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT_GENERAR_GARANTIA, CODIGO_PRODUCTO_SIN_GARANTIA_DISPONIBLE, CLIENTE_GARANTIA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_APPLICATION_JSON_CHARSET))
                .andExpect(content().json(errorResponse));
    }

    /**
     * Validar producto con garantia extendida asignada
     */
    @Test
    public void generarGarantiaDeProductoConGarantiaTest() throws Exception {

        String exceptionName = GarantiaExtendidaAsignadaException.class.getSimpleName();
        Error error = new Error(exceptionName, EL_PRODUCTO_TIENE_GARANTIA);
        String errorResponse = objectMapper.writeValueAsString(error);

        mvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT_GENERAR_GARANTIA, CODIGO_DE_PRODUCTO_CON_GARANTIA, CLIENTE_GARANTIA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(CONTENT_TYPE_APPLICATION_JSON_CHARSET))
                .andExpect(content().json(errorResponse));
    }

    /**
     * Validar producto con precio menor a 500_000
     */
    @Test
    public void generarGarantiaDeProductoConPrecioMenorTest() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT_GENERAR_GARANTIA, CODIGO_PRODUCTO_CON_PRECIO_MENOR, CLIENTE_GARANTIA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(ENDPOINT_CONSULTAR_GARANTIA, CODIGO_PRODUCTO_CON_PRECIO_MENOR)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_APPLICATION_JSON_CHARSET))
                .andReturn();

        GarantiaExtendida responseGarantia = objectMapper.readValue(
                result.getResponse().getContentAsString(), GarantiaExtendida.class
        );
        // Obtener fecha de solicitud esperada
        Date fechaSolicitudExpect = TODAY;
        // Obtener fecha de solicitud generada
        Date fechaSolicitudActual = responseGarantia.getFechaSolicitudGarantia();
        // Obtener fecha de finalización esperada
        Date fechaFinalizacionExpect = DateTestUtil.addDays(fechaSolicitudExpect, 100);
        // Obtener fecha de finalización actual
        Date fechaFinalizacionActual = responseGarantia.getFechaFinGarantia();

        // Validar respuesta del servicio
        Assert.assertEquals(CLIENTE_GARANTIA, responseGarantia.getNombreCliente());
        Assert.assertEquals(PRECIO_GARANTIA_MENOR, responseGarantia.getPrecioGarantia(), 0);
        Assert.assertTrue(DateTestUtil.isSameDate(fechaSolicitudExpect, fechaSolicitudActual));
        Assert.assertTrue(DateTestUtil.isSameDate(fechaFinalizacionExpect, fechaFinalizacionActual));
    }

    /**
     * Validar producto con precio mayor a 500_000
     */
    @Test
    public void generarGarantiaDeProductoConPrecioMayorTest() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT_GENERAR_GARANTIA, CODIGO_PRODUCTO_CON_PRECIO_MAYOR, CLIENTE_GARANTIA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(ENDPOINT_CONSULTAR_GARANTIA, CODIGO_PRODUCTO_CON_PRECIO_MAYOR)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_APPLICATION_JSON_CHARSET))
                .andReturn();

        GarantiaExtendida responseGarantia = objectMapper.readValue(
                result.getResponse().getContentAsString(), GarantiaExtendida.class
        );
        // Obtener fecha de solicitud esperada
        Date fechaSolicitudExpect = TODAY;
        // Obtener fecha de solicitud generada
        Date fechaSolicitudActual = responseGarantia.getFechaSolicitudGarantia();
        // Obtener fecha de finalización esperada
        Date fechaFinalizacionExpect = DateTestUtil.addDaysSkippingMondays(fechaSolicitudActual, 200);
        // Obtener fecha de finalización actual
        Date fechaFinalizacionActual = responseGarantia.getFechaFinGarantia();

        // Validar respuesta del servicio
        Assert.assertEquals(CLIENTE_GARANTIA, responseGarantia.getNombreCliente());
        Assert.assertEquals(PRECIO_GARANTIA_MAYOR, responseGarantia.getPrecioGarantia(), 0);
        Assert.assertTrue(DateTestUtil.isSameDate(fechaSolicitudExpect, fechaSolicitudActual));
        Assert.assertTrue(DateTestUtil.isSameDate(fechaFinalizacionExpect, fechaFinalizacionActual));
    }

}
