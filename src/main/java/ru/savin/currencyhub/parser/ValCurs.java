package ru.savin.currencyhub.parser;


import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ValCurs")
@Data
public class ValCurs {

    @XmlAttribute(name = "Date")
    private String localDate;

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "Valute")
    private List<Valute> valutes;

}
