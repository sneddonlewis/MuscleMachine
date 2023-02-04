package com.sneddsy.musclemachine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sneddsy.musclemachine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResistanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resistance.class);
        Resistance resistance1 = new Resistance();
        resistance1.setId(1L);
        Resistance resistance2 = new Resistance();
        resistance2.setId(resistance1.getId());
        assertThat(resistance1).isEqualTo(resistance2);
        resistance2.setId(2L);
        assertThat(resistance1).isNotEqualTo(resistance2);
        resistance1.setId(null);
        assertThat(resistance1).isNotEqualTo(resistance2);
    }
}
