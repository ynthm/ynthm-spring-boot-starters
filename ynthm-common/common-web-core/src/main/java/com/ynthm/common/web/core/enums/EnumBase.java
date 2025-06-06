package com.ynthm.common.web.core.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ynthm.common.api.Label;
import com.ynthm.common.enums.EnumValue;
import com.ynthm.common.web.core.enums.json.EnumeratorJsonDeserializer;
import com.ynthm.common.web.core.enums.json.EnumeratorJsonSerializer;
import java.io.Serializable;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@JsonSerialize(using = EnumeratorJsonSerializer.class)
@JsonDeserialize(using = EnumeratorJsonDeserializer.class)
public interface EnumBase<T extends Serializable> extends EnumValue<T>, Label {}
