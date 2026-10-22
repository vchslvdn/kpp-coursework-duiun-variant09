package ua.edu.nupp.kpp.server;

public class AuditObject {
    private Long id;
    private String code;
    private String name;
    private String address;

    public AuditObject() {
    }

    public AuditObject(Long id,
                       String code,
                       String name,
                       String address) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}