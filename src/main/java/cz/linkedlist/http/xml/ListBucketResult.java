package cz.linkedlist.http.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@XmlRootElement(name="ListBucketResult")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ListBucketResult {

    @XmlElement(name = "Name")
    private String name;
    @XmlElement(name = "Prefix")
    private String prefix;
    @XmlElement(name = "Marker")
    private String marker;
    @XmlElement(name = "MaxKeys")
    private int maxKeys;
    @XmlElement(name = "Delimiter")
    private String delimiter;
    @XmlElement(name = "IsTruncated")
    private boolean truncated;
    @XmlElement(name = "Contents")
    private List<Contents> contents;
    @XmlElement(name = "CommonPrefixes")
    private List<CommonPrefixes> commonPrefixes;
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
