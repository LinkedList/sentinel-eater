package cz.linkedlist.http.xml

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@XmlRootElement(name = "Contents")
@XmlAccessorType(XmlAccessType.FIELD)
class Contents (

    @XmlElement(name = "Key")
    val key: String? = null,
    @XmlElement(name = "Size")
    val size: Int = 0

)

/*
   <Contents>
      <Key>tiles/15/R/TM/2015/10/22/0/B07.jp2</Key>
      <LastModified>2016-12-15T19:33:24.000Z</LastModified>
      <ETag>&amp;quot;60e239daa5a7be90a5f5cd53df2a1ed8&amp;quot;</ETag>
      <Size>22738215</Size>
      <Owner>
         <ID>91d380b3cead28df927c824731b0173701336cd8d67b0679d7166288f3850f38</ID>
      </Owner>
      <StorageClass>STANDARD</StorageClass>
   </Contents>
 */
