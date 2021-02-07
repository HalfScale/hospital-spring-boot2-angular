package com.springboot.hospital.validator;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, SecondLevel.class, ThirdLevel.class})
public interface HospitalValidationSequence {

}
