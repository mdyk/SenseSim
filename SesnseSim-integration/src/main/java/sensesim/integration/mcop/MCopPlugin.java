package sensesim.integration.mcop;


public interface MCopPlugin {

    void start();

    void stop();

    void setCenter(double latitude, double longitude);

    void addUnit(Long unitId, String name, double latitude, double longitude);

    void updateUnitPosition(Long unitId, double latitude, double longitude);

    void addEquipment(Long unitId, Long equipmentId, String equipmentName, int quantity);

    boolean hasEquipment(Long unitId, Long equipmentId);

    void updateUnitEquipment(Long unitId, Long equipmentId, int quantity);

    void removeEquipment(Long unitId, Long equipmentId);

    void updateUnitSupplies();

}
