package com.avatech.edi.mdm.businessone.project;

import com.avatech.edi.mdm.bo.IProjectBudget;
import com.avatech.edi.mdm.bo.IProjectBudgetItem;
import com.avatech.edi.mdm.businessone.B1Exception;
import com.avatech.edi.mdm.businessone.BORepositoryBusinessOne;
import com.avatech.edi.mdm.businessone.config.B1Data;
import com.avatech.edi.mdm.config.B1Connection;
import com.sap.smb.sbo.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class B1ProjectBudgetServiceImp implements B1ProjectBudgetService {

    private final Logger logger = LoggerFactory.getLogger(B1ProjectBudgetServiceImp.class);


    private final String BASE_TYPE = "U_BaseType";
    private final String BASE_DOCENTRY = "U_BaseEntry";
    private final String BASE_LINENUM = "U_BaseLineNum";

    private final String OBJECT_CODE = "AVA_PM_TIMEBUDGET";
    @Override
    public String syncProjectBudget(IProjectBudget projectBudget, B1Connection b1Connection) {
        BORepositoryBusinessOne boRepositoryBusinessOne = null;
        ICompany company = null;
        try {
            boRepositoryBusinessOne = BORepositoryBusinessOne.getInstance(b1Connection);
            company = boRepositoryBusinessOne.getCompany();

            IStockTransfer document = SBOCOMUtil.newStockTransfer(company,SBOCOMConstants.BoObjectTypes_StockTransfer_oInventoryTransferRequest);
            document.setCardCode(B1Data.VISUAL_SUPPLIER);
            document.setDocDate(new Date());
            document.setTaxDate(new Date());
            if(projectBudget.getRemarks() != null)
                document.setComments(projectBudget.getRemarks());
            document.getUserFields().getFields().item(BASE_TYPE).setValue(OBJECT_CODE);
            document.getUserFields().getFields().item(BASE_DOCENTRY).setValue(projectBudget.getDocEntry());

            for (IProjectBudgetItem item:projectBudget.getProjectBudgetItemList()) {
                document.getLines().setItemCode(B1Data.VISUAL_ITEMCODE);
                document.getLines().setWarehouseCode(B1Data.VISUAL_WHSCODE1);
                document.getLines().setFromWarehouseCode(B1Data.VISUAL_WHSCODE2);

                document.getLines().getUserFields().getFields().item(BASE_TYPE).setValue(OBJECT_CODE);
                document.getLines().getUserFields().getFields().item(BASE_DOCENTRY).setValue(item.getDocEntry());
                document.getLines().getUserFields().getFields().item(BASE_LINENUM).setValue(item.getLineNum());
                document.getLines().add();
            }
            int rt = document.add();
            if(rt == 0 )
                return company.getNewObjectKey();
            else throw new B1Exception(company.getLastErrorCode() + ":" + company.getLastErrorDescription());
        }catch (SBOCOMException e){
            logger.error("同步项目预算发生异常",e);
            throw new B1Exception(e);
        }
    }
}