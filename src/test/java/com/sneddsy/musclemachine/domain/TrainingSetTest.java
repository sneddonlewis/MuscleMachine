package com.sneddsy.musclemachine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sneddsy.musclemachine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainingSetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingSet.class);
        TrainingSet trainingSet1 = new TrainingSet();
        trainingSet1.setId(1L);
        TrainingSet trainingSet2 = new TrainingSet();
        trainingSet2.setId(trainingSet1.getId());
        assertThat(trainingSet1).isEqualTo(trainingSet2);
        trainingSet2.setId(2L);
        assertThat(trainingSet1).isNotEqualTo(trainingSet2);
        trainingSet1.setId(null);
        assertThat(trainingSet1).isNotEqualTo(trainingSet2);
    }
}
