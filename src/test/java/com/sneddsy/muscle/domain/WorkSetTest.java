package com.sneddsy.muscle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sneddsy.muscle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkSetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkSet.class);
        WorkSet workSet1 = new WorkSet();
        workSet1.setId(1L);
        WorkSet workSet2 = new WorkSet();
        workSet2.setId(workSet1.getId());
        assertThat(workSet1).isEqualTo(workSet2);
        workSet2.setId(2L);
        assertThat(workSet1).isNotEqualTo(workSet2);
        workSet1.setId(null);
        assertThat(workSet1).isNotEqualTo(workSet2);
    }
}
