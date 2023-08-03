package io.kontakt.apps.temperature.generator;

import io.kontak.apps.temperature.generator.generator.ParametrizedTemperatureGenerator;
import io.kontak.apps.temperature.generator.TemperatureGeneratorApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.*;
import static org.testcontainers.shaded.org.hamcrest.core.Is.is;

@SpringBootTest(classes = TemperatureGeneratorApplication.class)
public class ParametrizedTemperatureGeneratorTest {

    @Autowired
    private ParametrizedTemperatureGenerator parametrizedTemperatureGenerator;

    @Test
    public void testParametrizedTemperatureGeneratesDesiredData() {
        //given
        //when
        var generatedTemperatures = parametrizedTemperatureGenerator.generate();

        //then
        Assertions.assertNotNull(generatedTemperatures);
        Assertions.assertEquals(5, generatedTemperatures.size());
        assertThat(generatedTemperatures.get(0).temperature(), is(both(greaterThan(19.0)).and(lessThan(22.0))));
        assertThat(generatedTemperatures.get(1).temperature(), is(both(greaterThan(19.0)).and(lessThan(22.0))));
        assertThat(generatedTemperatures.get(2).temperature(), is(both(greaterThan(19.0)).and(lessThan(22.0))));
        assertThat(generatedTemperatures.get(3).temperature(), is(anyOf(
                both(greaterThan(9.5)).and(lessThan(15.0)),
                both(greaterThan(26.0)).and(lessThan(31.5))
        )));
        assertThat(generatedTemperatures.get(4).temperature(), is(both(greaterThan(19.0)).and(lessThan(22.0))));
    }
}
