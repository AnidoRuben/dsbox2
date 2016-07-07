package es.uvigo.esei.dsbox.virtualbox.execution;

import es.uvigo.esei.dsbox.core.model.execution.VMDriverSpec;
import es.uvigo.esei.dsbox.core.model.execution.VMType;
import es.uvigo.esei.dsbox.virtualbox.manager.VBoxWebserverManager;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"vboxVersion", "simulationsBasePath", "imagesBasePath", "bridgedInterface", "webServerHost", "webServerPort", "webServerUser", "webServerPassword"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="virtualbox-driver-spec")
public class VirtualBoxDriverSpec extends VMDriverSpec {

    @XmlAttribute(name = "vbox-version")
    private String vboxVersion;

    @XmlElement(name = "simulations-base-path")
    private String simulationsBasePath;

    @XmlElement(name = "images-base-path")
    private String imagesBasePath;

    @XmlElement(name = "bridged-interface")
    private String bridgedInterface;

    @XmlElement(name = "web-server-host")
    private String webServerHost;

    @XmlElement(name = "web-server-port")
    private int webServerPort;

    @XmlElement(name = "web-server-user")
    private String webServerUser;

    @XmlElement(name = "web-server-password")
    private String webServerPassword;

    // Mas cosas -> no sé cuales
    public VirtualBoxDriverSpec() {
        super(VMType.VIRTUALBOX);
        this.vboxVersion = "VirtualBox 5.0.x";
        this.webServerHost = VBoxWebserverManager.DEFAULT_VBOX_WEBSERVER_HOST;
        this.webServerPort = VBoxWebserverManager.DEFAULT_VBOX_WEBSERVER_PORT;
        this.webServerUser = "";
        this.webServerPassword = "";
        this.bridgedInterface = "";
    }

    public VirtualBoxDriverSpec(String simulationsBasePath, String imagesBasePath) {
        super(VMType.VIRTUALBOX);
        this.simulationsBasePath = simulationsBasePath;
        this.imagesBasePath = imagesBasePath;
    }

    public String getVboxVersion() {
        return vboxVersion;
    }

    public void setVboxVersion(String vboxVersion) {
        this.vboxVersion = vboxVersion;
    }

    public String getSimulationsBasePath() {
        return simulationsBasePath;
    }

    public void setSimulationsBasePath(String simulationsBasePath) {
        this.simulationsBasePath = simulationsBasePath;
    }

    public String getImagesBasePath() {
        return imagesBasePath;
    }

    public void setImagesBasePath(String imagesBasePath) {
        this.imagesBasePath = imagesBasePath;
    }

    public String getBridgedInterface() {
        return bridgedInterface;
    }

    public void setBridgedInterface(String bridgedInterface) {
        this.bridgedInterface = bridgedInterface;
    }

    public String getWebServerHost() {
        return webServerHost;
    }

    public void setWebServerHost(String webServerHost) {
        this.webServerHost = webServerHost;
    }

    public int getWebServerPort() {
        return webServerPort;
    }

    public void setWebServerPort(int webServerPort) {
        this.webServerPort = webServerPort;
    }

    public String getWebServerUser() {
        return webServerUser;
    }

    public void setWebServerUser(String webServerUser) {
        this.webServerUser = webServerUser;
    }

    public String getWebServerPassword() {
        return webServerPassword;
    }

    public void setWebServerPassword(String webServerPassword) {
        this.webServerPassword = webServerPassword;
    }

}
