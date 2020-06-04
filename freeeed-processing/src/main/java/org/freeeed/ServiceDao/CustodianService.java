package org.freeeed.ServiceDao;

import org.freeeed.Entity.Project;
import org.freeeed.Entity.ProjectCustodian;


public class CustodianService {

    private static volatile CustodianService mInstance;

    private CustodianService() {
    }

    public static CustodianService getInstance() {
        if (mInstance == null) {
            synchronized (CustodianService.class) {
                if (mInstance == null) {
                    mInstance = new CustodianService();
                }
            }
        }
        return mInstance;
    }

    public ProjectCustodian getCustodianByName(String name, Project prj) {
        ProjectCustodian custodian = null;
        try {
            custodian = CustodianDao.getInstance().getCustodianByName(name, prj);
        } catch (Exception e) {
            custodian = CustodianDao.getInstance().createCustodian(new ProjectCustodian(name, prj));
        }
        return custodian;
    }
}
