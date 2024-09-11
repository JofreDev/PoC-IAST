package co.com.bancolombia.iastpattern.config.json_dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

@Getter
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({

})
public class JsonValue {


}
