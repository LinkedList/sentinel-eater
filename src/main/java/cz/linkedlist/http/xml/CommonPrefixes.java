package cz.linkedlist.http.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@XmlRootElement(name = "CommonPrefixes")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class CommonPrefixes {

    @XmlElement(name = "Prefix")
    private String prefix;

}
