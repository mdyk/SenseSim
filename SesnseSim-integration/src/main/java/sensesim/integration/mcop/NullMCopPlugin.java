package sensesim.integration.mcop;


public class NullMCopPlugin implements MCopPlugin {
    @Override
    public void start() {
        
    }

    @Override
    public void stop() {

    }

    @Override
    public void setCenter(double latitude, double longitude) {
        
    }

    @Override
    public void addUnit(Long unitId, String name, double latitude, double longitude) {

    }

    @Override
    public void updateUnitPosition(Long unitId, double latitude, double longitude) {

    }

    @Override
    public void addEquipment(Long unitId, Long equipmentId, String equipmentName, int quantity) {

    }

    @Override
    public boolean hasEquipment(Long unitId, Long equipmentId) {
        return false;
    }

    @Override
    public void updateUnitEquipment(Long unitId, Long equipmentId, int quantity) {

    }

    @Override
    public void updateUnitHealthStatus(Long unitId, String status) {
        
    }

    @Override
    public void addSupply(Long unitId, Long supplyId, String supplyName, double amount) {
        
    }

    @Override
    public void updateSupply(Long unitId, Long supplyId, double amount) {

    }

    @Override
    public void removeEquipment(Long unitId, Long equipmentId) {

    }
}
