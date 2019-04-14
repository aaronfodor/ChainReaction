package presenter;

/**
 * Represents a PlayerList element
 */
public class PlayerListData{

    /**
     * Name of Player
     */
    private String name;

    /**
     * Type of Player
     */
    private String type;

    /**
     * Image Id of Player
     */
    private int imgId;

    /**
     * constructor of PlayerListData
     *
     * @param   name    Name of the Player
     * @param   type    Type of the Player
     * @param   imgId   Image Id of the Player
     */
    public PlayerListData(String name, String type, int imgId) {
        this.name = name;
        this.type = type;
        this.imgId = imgId;
    }

    /**
     * Name getter method
     *
     * @return 	name	Name of the Player
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter method
     *
     * @param   name 	Name of the Player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Type getter method
     *
     * @return 	String	Type of the Player
     */
    public String getType() {
        return type;
    }

    /**
     * Type setter method
     *
     * @param   type    Type of the Player
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Image Id getter method
     *
     * @return 	int 	Id of Image
     */
    public int getImgId() {
        return imgId;
    }

    /**
     * Image Id setter method
     *
     * @param   imgId 	    Id of the Image
     */
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

}  