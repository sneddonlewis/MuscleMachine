package com.sneddsy.musclemachine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sneddsy.musclemachine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StrengthWorkoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StrengthWorkout.class);
        StrengthWorkout strengthWorkout1 = new StrengthWorkout();
        strengthWorkout1.setId(1L);
        StrengthWorkout strengthWorkout2 = new StrengthWorkout();
        strengthWorkout2.setId(strengthWorkout1.getId());
        assertThat(strengthWorkout1).isEqualTo(strengthWorkout2);
        strengthWorkout2.setId(2L);
        assertThat(strengthWorkout1).isNotEqualTo(strengthWorkout2);
        strengthWorkout1.setId(null);
        assertThat(strengthWorkout1).isNotEqualTo(strengthWorkout2);
    }
}
