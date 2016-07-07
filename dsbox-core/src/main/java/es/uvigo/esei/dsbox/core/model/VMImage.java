package es.uvigo.esei.dsbox.core.model;

import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(propOrder = {"imageType", "creationDate", "downloaded", "compressed", "compressorType", "downloadURI", "localURI"})
@XmlAccessorType(XmlAccessType.FIELD)
public class VMImage {

    @XmlAttribute(name = "creation-date")
    private Date creationDate;

    @XmlAttribute(name = "image-type")
    private ImageType imageType;

    @XmlElement(name = "download-uri")
    private String downloadURI;

    @XmlElement(name = "local-uri")
    private String localURI;
    
    @XmlAttribute
    private boolean downloaded;

    @XmlAttribute(name="is-compressed")
    private boolean compressed;
    
    @XmlAttribute(name="compressor-type")
    private CompressorType compressorType;
    
    
    
    public VMImage() {
        this.creationDate = Calendar.getInstance().getTime();
        this.downloaded = false;
        this.compressorType = CompressorType.NONE;
        this.compressed = false;
    }
    
    public VMImage(ImageType imageType, String downloadURI) {
        this();       
        this.imageType = imageType;
        this.downloadURI = downloadURI;
        if (downloadURI.endsWith(".gz")) {
           this.compressed = true;
           this.compressorType = CompressorType.GZIP;
        }
    }

    public VMImage(ImageType imageType, String downloadURI, String localURI) {
        this(imageType, downloadURI);
        this.localURI = localURI;
        if (localURI.endsWith(".gz")) {
           this.compressed = true;
           this.compressorType = CompressorType.GZIP;
        }        
        this.downloaded = true;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public String getDownloadURI() {
        return downloadURI;
    }

    public void setDownloadURI(String downloadURI) {
        this.downloadURI = downloadURI;
    }

    public String getLocalURI() {
        return localURI;
    }

    public void setLocalURI(String localURI) {
        this.localURI = localURI;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public CompressorType getCompressorType() {
        return compressorType;
    }

    public void setCompressorType(CompressorType compressorType) {
        this.compressorType = compressorType;
        if (compressorType == CompressorType.NONE) {
            this.compressed = false;
        }
    }
    
    
    
    

    @XmlEnum(String.class)
    public static enum ImageType {
        VDI, VMDK, QEMU
    }
    
    @XmlEnum(String.class)
    public static enum CompressorType {
        NONE, GZIP, ZIP
    }    
}
