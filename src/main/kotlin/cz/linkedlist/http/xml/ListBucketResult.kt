package cz.linkedlist.http.xml

import java.util.*
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
@XmlRootElement(name = "ListBucketResult")
@XmlAccessorType(XmlAccessType.FIELD)
data class ListBucketResult(

    @XmlElement(name = "Name")
    val name: String? = null,
    @XmlElement(name = "Prefix")
    val prefix: String? = null,
    @XmlElement(name = "Marker")
    val marker: String? = null,
    @XmlElement(name = "MaxKeys")
    val maxKeys: Int = 0,
    @XmlElement(name = "Delimiter")
    val delimiter: String? = null,
    @XmlElement(name = "IsTruncated")
    val truncated: Boolean = false,
    @XmlElement(name = "Contents")
    val contents : Array<Contents> = emptyArray(),
    @XmlElement(name = "CommonPrefixes")
    val commonPrefixes: Array<CommonPrefixes> = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListBucketResult

        if (name != other.name) return false
        if (prefix != other.prefix) return false
        if (marker != other.marker) return false
        if (maxKeys != other.maxKeys) return false
        if (delimiter != other.delimiter) return false
        if (truncated != other.truncated) return false
        if (!Arrays.equals(contents, other.contents)) return false
        if (!Arrays.equals(commonPrefixes, other.commonPrefixes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (prefix?.hashCode() ?: 0)
        result = 31 * result + (marker?.hashCode() ?: 0)
        result = 31 * result + maxKeys
        result = 31 * result + (delimiter?.hashCode() ?: 0)
        result = 31 * result + truncated.hashCode()
        result = 31 * result + Arrays.hashCode(contents)
        result = 31 * result + Arrays.hashCode(commonPrefixes)
        return result
    }
}

/*
<?xml version="1.0" encoding="UTF-8"?>
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
   <Name>sentinel-s2-l1c</Name>
   <Prefix>tiles/15/R/TM/2015/10/22/0/</Prefix>
   <Marker />
   <MaxKeys>1000</MaxKeys>
   <Delimiter>/</Delimiter>
   <IsTruncated>false</IsTruncated>
   <Contents>
      <Key>tiles/15/R/TM/2015/10/22/0/B01.jp2</Key>
      <LastModified>2016-12-15T19:33:23.000Z</LastModified>
      <ETag>&amp;quot;ad5344c14e9871b953d42eb78cf1aba4&amp;quot;</ETag>
      <Size>2818717</Size>
      <Owner>
         <ID>91d380b3cead28df927c824731b0173701336cd8d67b0679d7166288f3850f38</ID>
      </Owner>
      <StorageClass>STANDARD</StorageClass>
   </Contents>
   <CommonPrefixes>
      <Prefix>tiles/15/R/TM/2015/10/22/0/auxiliary/</Prefix>
   </CommonPrefixes>
</ListBucketResult>
 */
