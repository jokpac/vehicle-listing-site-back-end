package lt.ca.javau11.models;

public class CityDTO {
	
    private Long id;
    private String name;

    public CityDTO() {}
    
    public CityDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
