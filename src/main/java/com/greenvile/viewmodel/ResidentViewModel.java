/*does crud for our residents. Crud if u forgor is the Create, Read Update and delete. 
This is also were we generate the ID for residents automatically.
 We never see it but its there and app knows it. 
 Other words dont fuck with this class too muuch */
package com.greenvile.viewmodel;

import com.greenvile.model.*;
import java.util.ArrayList;
import java.util.List;

public class ResidentViewModel {
    private DataManager dataManager;

    public ResidentViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Resident> getResidentList() {
        return dataManager.getResidents();
    }

    public void addResident(Resident resident) {
        resident.setId(dataManager.generateNewResidentId());
        dataManager.getResidents().add(resident);
    }

    public void updateResident(Resident resident) {
        for (int i = 0; i < dataManager.getResidents().size(); i++) {
            if (dataManager.getResidents().get(i).getId() == resident.getId()) {
                dataManager.getResidents().set(i, resident);
                break;
            }
        }
    }

    public void deleteResident(int id) {
        dataManager.getResidents().removeIf(r -> r.getId() == id);
    }

    public List<Resident> searchResidents(String query) {
        List<Resident> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Resident r : dataManager.getResidents()) {
            if (r.getFullName().toLowerCase().contains(lowerQuery)) {
                results.add(r);
            }
        }
        return results;
    }
}
