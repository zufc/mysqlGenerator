package com.cbecs.generator.entity;

import com.cbecs.generator.util.Utililies;

public class CreAttr implements Cloneable
{
    private String daoFrame;
    private String conFrame;
    private String entityName;
    private String entityPackage;
    private String entityFilePath;
    private String daoName;
    private String daoPackage;
    private String daoFilePath;
    private String mapperName;
    private String mapperPackage;
    private String mapperFilePath;
    private String serviceName;
    private String serviceImplName;
    private String servicePackage;
    private String serviceFilePath;
    private String serviceImplPackage;
    private String serviceImplFilePath;
    private String controllerName;
    private String controllerPackage;
    private String controllerFilePath;
    private String saveDir;
    private String saveDir2;

    public void replaceAll(String table)
    {
        this.entityName = Utililies.tableToEntity(table);
        this.entityPackage = Utililies.parseTemplate(this.entityPackage, "EntityName", this.entityName);
        this.entityFilePath = Utililies.parseTemplate(this.entityFilePath, "EntityName", this.entityName);

        this.daoName = Utililies.parseTemplate(this.daoName, "EntityName", this.entityName);
        this.daoPackage = Utililies.parseTemplate(this.daoPackage, "EntityName", this.entityName);
        this.daoFilePath = Utililies.parseTemplate(this.daoFilePath, "EntityName", this.entityName);

        this.mapperName = Utililies.parseTemplate(this.mapperName, "EntityName", this.entityName);
        this.mapperPackage = Utililies.parseTemplate(this.mapperPackage, "EntityName", this.entityName);
        this.mapperFilePath = Utililies.parseTemplate(this.mapperFilePath, "EntityName", this.entityName);

        this.serviceName = Utililies.parseTemplate(this.serviceName, "EntityName", this.entityName);
        this.servicePackage = Utililies.parseTemplate(this.servicePackage, "EntityName", this.entityName);
        this.serviceFilePath = Utililies.parseTemplate(this.serviceFilePath, "EntityName", this.entityName);
        this.serviceImplName = Utililies.parseTemplate(this.serviceImplName, "EntityName", this.entityName);
        this.serviceImplPackage = Utililies.parseTemplate(this.serviceImplPackage, "EntityName", this.entityName);
        this.serviceImplFilePath = Utililies.parseTemplate(this.serviceImplFilePath, "EntityName", this.entityName);

        this.controllerName = Utililies.parseTemplate(this.controllerName, "EntityName", this.entityName);
        this.controllerPackage = Utililies.parseTemplate(this.controllerPackage, "EntityName", this.entityName);
        this.controllerFilePath = Utililies.parseTemplate(this.controllerFilePath, "EntityName", this.entityName);

    }

    public CreAttr clone()
    {
        CreAttr o = null;
        try
        {
            o = (CreAttr) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return o;
    }

    public String getDaoFrame()
    {
        return daoFrame;
    }

    public void setDaoFrame(String daoFrame)
    {
        this.daoFrame = daoFrame;
    }

    public String getConFrame()
    {
        return conFrame;
    }

    public void setConFrame(String conFrame)
    {
        this.conFrame = conFrame;
    }

    public String getEntityName()
    {
        return entityName;
    }

    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    public String getEntityPackage()
    {
        return entityPackage;
    }

    public void setEntityPackage(String entityPackage)
    {
        this.entityPackage = entityPackage;
    }

    public String getEntityFilePath()
    {
        return entityFilePath;
    }

    public void setEntityFilePath(String entityFilePath)
    {
        this.entityFilePath = entityFilePath;
    }

    public String getDaoName()
    {
        return daoName;
    }

    public void setDaoName(String daoName)
    {
        this.daoName = daoName;
    }

    public String getDaoPackage()
    {
        return daoPackage;
    }

    public void setDaoPackage(String daoPackage)
    {
        this.daoPackage = daoPackage;
    }

    public String getDaoFilePath()
    {
        return daoFilePath;
    }

    public void setDaoFilePath(String daoFilePath)
    {
        this.daoFilePath = daoFilePath;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getServiceImplName()
    {
        return serviceImplName;
    }

    public void setServiceImplName(String serviceImplName)
    {
        this.serviceImplName = serviceImplName;
    }

    public String getServicePackage()
    {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage)
    {
        this.servicePackage = servicePackage;
    }

    public String getServiceFilePath()
    {
        return serviceFilePath;
    }

    public void setServiceFilePath(String serviceFilePath)
    {
        this.serviceFilePath = serviceFilePath;
    }

    public String getServiceImplPackage()
    {
        return serviceImplPackage;
    }

    public void setServiceImplPackage(String serviceImplPackage)
    {
        this.serviceImplPackage = serviceImplPackage;
    }

    public String getServiceImplFilePath()
    {
        return serviceImplFilePath;
    }

    public void setServiceImplFilePath(String serviceImplFilePath)
    {
        this.serviceImplFilePath = serviceImplFilePath;
    }

    public String getControllerName()
    {
        return controllerName;
    }

    public void setControllerName(String controllerName)
    {
        this.controllerName = controllerName;
    }

    public String getControllerPackage()
    {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage)
    {
        this.controllerPackage = controllerPackage;
    }

    public String getControllerFilePath()
    {
        return controllerFilePath;
    }

    public void setControllerFilePath(String controllerFilePath)
    {
        this.controllerFilePath = controllerFilePath;
    }

    public String getSaveDir()
    {
        return saveDir;
    }

    public void setSaveDir(String saveDir)
    {
        this.saveDir = saveDir;
    }

    public String getSaveDir2()
    {
        return saveDir2;
    }

    public void setSaveDir2(String saveDir2)
    {
        this.saveDir2 = saveDir2;
    }

    public String getMapperName()
    {
        return mapperName;
    }

    public void setMapperName(String mapperName)
    {
        this.mapperName = mapperName;
    }

    public String getMapperPackage()
    {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage)
    {
        this.mapperPackage = mapperPackage;
    }

    public String getMapperFilePath()
    {
        return mapperFilePath;
    }

    public void setMapperFilePath(String mapperFilePath)
    {
        this.mapperFilePath = mapperFilePath;
    }

}
