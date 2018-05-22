package cz.linkedlist.http.xml

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@XmlRootElement(name = "CommonPrefixes")
@XmlAccessorType(XmlAccessType.FIELD)
class CommonPrefixes(
    @XmlElement(name = "Prefix")
    val prefix: String? = null
)

