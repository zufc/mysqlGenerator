package com.cbecs.generator.main;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.cbecs.generator.db.DBHelper;
import com.cbecs.generator.entity.CreAttr;
import com.cbecs.generator.entity.DbTableColumn;
import com.cbecs.generator.entity.MysqlDbColumn;
import com.cbecs.generator.form.MySqlSetPanel;
import com.cbecs.generator.util.CommonUtils;
import com.cbecs.generator.util.Consts;
import com.cbecs.generator.util.Utililies;

public class Creater
{
    private static Logger log = Logger.getLogger(Creater.class);

    CreAttr ca = null;
    String tableName = "";
    List<DbTableColumn> columnList = null;
    String dir = "template/";
    private String entityName = null;
    private String primaryKey = null;
    private String primaryColumn = null;
    private String primaryKeyJdbcType = null;
    private String primaryKeyJaveType = null;
    private boolean hasDate = false;
    private boolean hasTimestamp = false;

    public Creater(CreAttr ca, String tname, List<DbTableColumn> columnList)
    {
        this.ca = ca;
        this.tableName = tname;
        this.columnList = columnList;
        getEntityName();
    }

    public String getEntityName()
    {
        if (entityName == null)
        {
            entityName = Utililies.tableToEntity(tableName);
            ca.replaceAll(tableName);
        }
        return entityName;
    }

    public String getTlpPath(String name)
    {
        String fix = "mysql_";
        if (DBHelper.sections.equals(MySqlSetPanel.sections))
        {
            fix = "mysql_";
        }
        return dir + fix + name;
    }

    public void run()
    {
        createEntity();
        // 持久层框架
        if (ca.getDaoFrame().equalsIgnoreCase("mybatis"))
        {
            createDaoMapper();
            createService();
            createServiceImpl();
        }
        // 控制层框架
        if (ca.getConFrame().equalsIgnoreCase("SpringMVC"))
        {
            createController();
        }
    }

    public String createEntity()
    {
        String javaPath = Utililies.parseFilePath(ca.getSaveDir(), ca.getEntityFilePath());
        String result = "";
        try
        {
            File javaFile = new File(javaPath);
            if (javaFile.exists())
            {
                javaFile.delete();
            }
            String content = Utililies.readResourceFile(dir + "entity.tlp");
            // package
            content = Utililies.parseTemplate(content, "package", Utililies.getSuperPackage(ca.getEntityPackage()));
            // EntityName
            content = Utililies.parseTemplate(content, "EntityName", getEntityName());
            content = Utililies.parseTemplate(content, "TableName", tableName);
            // attr_list
            hasDate = false;
            hasTimestamp = false;
            content = Utililies.parseTemplate(content, "attr_list", createAttrList());
            content = Utililies.parseTemplate(content, "importDate", hasDate ? Consts.IMPORT_DATE : "");
            content = Utililies.parseTemplate(content, "importTimestamp", hasTimestamp ? Consts.IMPORT_TIMESTAMP : "");
            // attr_getset_list
            content = Utililies.parseTemplate(content, "attr_getset_list", createAttrGetsetList());

            content = Utililies.parseTemplate(content, "attr_tostring_list", createAttrToStringList());

            Utililies.writeContentToFile(Utililies.parseFilePath(ca.getSaveDir(), ca.getEntityFilePath()), content);

        }
        catch (Exception e)
        {
            result = "实体生成失败：" + e.getMessage();
            log.info(result);
        }
        return result;
    }

    public String createDaoMapper()
    {
        String daoPath = Utililies.parseFilePath(ca.getSaveDir(), ca.getDaoFilePath());
        String mapperPath = Utililies.parseFilePath(ca.getSaveDir(), ca.getMapperFilePath());
        String result = "";
        try
        {
            File daoFile = new File(daoPath);
            if (daoFile.exists())
            {
                daoFile.delete();
            }

            File mapperFile = new File(mapperPath);
            if (mapperFile.exists())
            {
                mapperFile.delete();
            }

            String content = Utililies.readResourceFile(getTlpPath("dao.tlp"));
            // package
            content = Utililies.parseTemplate(content, "DaoSuperPackage", Utililies.getSuperPackage(ca.getDaoPackage()));
            content = Utililies.parseTemplate(content, "DaoName", ca.getDaoName());
            content = Utililies.parseTemplate(content, "EntityName", ca.getEntityName());
            content = Utililies.parseTemplate(content, "TableName", tableName);
            content = Utililies.parseTemplate(content, "FeildJoin", getFeildJoin());
            content = Utililies.parseTemplate(content, "FeildJoinAll", getAllFeildJoin());
            content = Utililies.parseTemplate(content, "FeildMapJoin", getFeildMapJoin());
            content = Utililies.parseTemplate(content, "Key", getKeyJoin());
            content = Utililies.parseTemplate(content, "FeildUp", getFeildUp());
            content = Utililies.parseTemplate(content, "EntityPackage", ca.getEntityPackage());
            content = Utililies.parseTemplate(content, "returnKeyId", getPrimaryKey().isEmpty() ? "" : "<selectKey keyProperty=\"" + Utililies.columnToFeild(getPrimaryKey()) + "\" order=\"AFTER\" resultType=\"int\">SELECT @@IDENTITY</selectKey>");
            content = Utililies.parseTemplate(content, "keyModels", getKeyModels());
            
            String mapperContent = Utililies.readResourceFile(getTlpPath("mapper.tlp"));
            // package
            mapperContent = Utililies.parseTemplate(mapperContent, "DaoSuperPackage", Utililies.getSuperPackage(ca.getDaoPackage()));
            mapperContent = Utililies.parseTemplate(mapperContent, "DaoName", ca.getDaoName());
            mapperContent = Utililies.parseTemplate(mapperContent, "EntityName", ca.getEntityName());
            mapperContent = Utililies.parseTemplate(mapperContent, "EntityPackage", ca.getEntityPackage());
            mapperContent = Utililies.parseTemplate(mapperContent, "TableName", tableName);
            mapperContent = Utililies.parseTemplate(mapperContent, "FeildJoin", getFeildJoin());
            mapperContent = Utililies.parseTemplate(mapperContent, "FeildJoinAll", getAllFeildJoin());
            mapperContent = Utililies.parseTemplate(mapperContent, "FeildMapJoin", getFeildMapJoin());
            mapperContent = Utililies.parseTemplate(mapperContent, "Key", getKeyJoin());
            mapperContent = Utililies.parseTemplate(mapperContent, "FeildUp", getFeildUp());
            mapperContent = Utililies.parseTemplate(mapperContent, "EntityPackage", ca.getEntityPackage());
            mapperContent = Utililies.parseTemplate(mapperContent, "returnKeyId", getPrimaryKey().isEmpty() ? "" : "<selectKey keyProperty=\"" + Utililies.columnToFeild(getPrimaryKey()) + "\" order=\"AFTER\" resultType=\"int\">SELECT @@IDENTITY</selectKey>");
            mapperContent = Utililies.parseTemplate(mapperContent, "keyModels", getKeyModels());

            Utililies.writeContentToFile(daoPath, content);
            Utililies.writeContentToFile(mapperPath, mapperContent);
        }
        catch (Exception e)
        {
            result = ca.getDaoName() + "类生成失败：" + e.getMessage();
            log.info(result);
        }
        return result;
    }

    public String createService()
    {
        String javaPath = Utililies.parseFilePath(ca.getSaveDir(), ca.getServiceFilePath());
        String result = "";
        try
        {
            File javaFile = new File(javaPath);
            if (javaFile.exists())
            {
                javaFile.delete();
            }
            String content = Utililies.readResourceFile(getTlpPath("service.tlp"));
            // package
            content = Utililies.parseTemplate(content, "ServiceSuperPackage", Utililies.getSuperPackage(ca.getServicePackage()));
            content = Utililies.parseTemplate(content, "EntitySupperPackage", Utililies.getSuperPackage(ca.getEntityPackage()));
            content = Utililies.parseTemplate(content, "EntityPackage", ca.getEntityPackage());
            content = Utililies.parseTemplate(content, "ServiceName", ca.getServiceName());
            content = Utililies.parseTemplate(content, "EntityName", getEntityName());
            content = Utililies.parseTemplate(content, "keyModels", getKeyModels());

            Utililies.writeContentToFile(javaPath, content);
        }
        catch (Exception e)
        {
            result = ca.getEntityName() + "接口生成失败：" + e.getMessage();
            log.info(result);
        }
        return result;
    }

    public String createServiceImpl()
    {
        String javaPath = Utililies.parseFilePath(ca.getSaveDir(), ca.getServiceImplFilePath());
        String result = "";
        try
        {
            File javaFile = new File(javaPath);
            if (javaFile.exists())
            {
                javaFile.delete();
            }
            String content = Utililies.readResourceFile(getTlpPath("serviceImpl.tlp"));
            // package
            content = Utililies.parseTemplate(content, "ServiceImplSuperPackage", Utililies.getSuperPackage(ca.getServiceImplPackage()));
            content = Utililies.parseTemplate(content, "EntitySupperPackage", Utililies.getSuperPackage(ca.getEntityPackage()));
            content = Utililies.parseTemplate(content, "DaoSuperPackage", ca.getDaoPackage());
            content = Utililies.parseTemplate(content, "EntityPackage", ca.getEntityPackage());
            content = Utililies.parseTemplate(content, "EntityName", getEntityName());
            content = Utililies.parseTemplate(content, "DaoName", ca.getDaoName());
            content = Utililies.parseTemplate(content, "lDaoName", CommonUtils.firstCharToLowerCase(ca.getDaoName()));
            content = Utililies.parseTemplate(content, "ServicePackage", ca.getServicePackage());
            content = Utililies.parseTemplate(content, "ServiceName", ca.getServiceName());
            content = Utililies.parseTemplate(content, "keyModels", getKeyModels());
            content = Utililies.parseTemplate(content, "keys", getPrimaryKey());

            Utililies.writeContentToFile(javaPath, content);
        }
        catch (Exception e)
        {
            result = ca.getServiceName() + "类生成失败：" + e.getMessage();
            log.info(result);
        }
        return result;
    }

    public String createController()
    {
        String javaPath = Utililies.parseFilePath(ca.getSaveDir(), ca.getControllerFilePath());
        String result = "";
        try
        {
            File javaFile = new File(javaPath);
            if (javaFile.exists())
            {
                javaFile.delete();
            }
            String content = Utililies.readResourceFile(getTlpPath("controller.tlp"));
            content = Utililies.parseTemplate(content, "ControllerSuperPackage", Utililies.getSuperPackage(ca.getControllerPackage()));
            content = Utililies.parseTemplate(content, "ControllerNameLower", CommonUtils.firstCharToLowerCase(ca.getControllerName()));
            content = Utililies.parseTemplate(content, "ControllerName", ca.getControllerName());
            content = Utililies.parseTemplate(content, "ServiceName", ca.getServiceName());
            content = Utililies.parseTemplate(content, "ServiceNameLower", CommonUtils.firstCharToLowerCase(ca.getServiceName()));
            content = Utililies.parseTemplate(content, "EntityName", getEntityName());
            content = Utililies.parseTemplate(content, "EntityNameLower", getEntityName().toLowerCase());
            content = Utililies.parseTemplate(content, "EntityPackage", ca.getEntityPackage());
            content = Utililies.parseTemplate(content, "ServicePackage", ca.getServicePackage());
            content = Utililies.parseTemplate(content, "keys", getPrimaryKey());
            content = Utililies.parseTemplate(content, "keyModels", getKeyModels());
            Utililies.writeContentToFile(javaPath, content);
        }
        catch (Exception e)
        {
            result = ca.getControllerName() + "生成失败：" + e.getMessage();
            log.info(result);
        }
        return result;
    }

    public String createAttrList()
    {
        StringBuffer sb = new StringBuffer();
        if (columnList != null)
        {
            MysqlDbColumn column = null;
            for (int i = 0, k = columnList.size(); i < k; i++)
            {
                column = (MysqlDbColumn) columnList.get(i);
                if (column != null)
                {
                    if (column.getDataType().equalsIgnoreCase("date") || column.getDataType().equalsIgnoreCase("timestamp") || column.getDataType().equalsIgnoreCase("datetime") || column.getDataType().equalsIgnoreCase("time"))
                    {
                        hasDate = true;
                    }
                    if (column.getDataType().equalsIgnoreCase("timestamp") || column.getDataType().equalsIgnoreCase("time"))
                    {
                        hasTimestamp = true;
                    }
                    sb.append(Consts.TAB4).append("/** ").append(Consts.ENTER).append(Consts.TAB5).append(" *").append(column.getColumnName()).append(column.getColumnComment()).append(Consts.ENTER).append(Consts.TAB5).append(" */").append(Consts.ENTER).append(!column.isPrimaryKey() ? "" + Consts.TAB4 : "" + Consts.TAB4).append(Utililies.getAttrDeclare(Utililies.getVarJavaType(column.getDataType()), Utililies.columnToFeild(column.getColumnName()), column.getColumnDefault())).append(Consts.ENTER).append(Consts.ENTER);
                }
            }
        }
        return sb.toString();
    }

    public String createAttrGetsetList()
    {
        StringBuffer sb = new StringBuffer();
        if (columnList != null)
        {
            MysqlDbColumn column = null;
            String content = null, attr = null;
            for (int i = 0, k = columnList.size(); i < k; i++)
            {
                column = (MysqlDbColumn) columnList.get(i);
                if (column != null)
                {
                    attr = Utililies.tableToEntity(column.getColumnName());
                    content = Utililies.readResourceFile(dir + "getset.tlp");
                    content = Utililies.parseTemplate(content, "EntityName", getEntityName());
                    content = Utililies.parseTemplate(content, "AttrName", CommonUtils.firstCharToUpperCase(attr));
                    content = Utililies.parseTemplate(content, "attrName", CommonUtils.firstCharToLowerCase(attr));
                    content = Utililies.parseTemplate(content, "comment", CommonUtils.isBlank(column.getColumnComment()) ? column.getColumnName() : column.getColumnComment());
                    content = Utililies.parseTemplate(content, "JavaType", Utililies.getJavaType(column.getDataType()));

                    sb.append(content).append(Consts.ENTER);
                }
            }
        }
        return sb.toString();
    }

    public String createAttrToStringList()
    {
        StringBuffer sb = new StringBuffer();
        if (columnList != null)
        {
            MysqlDbColumn mdc = null;
            for (int i = 0, k = columnList.size(); i < k; i++)
            {
                mdc = (MysqlDbColumn) columnList.get(i);
                sb.append(mdc.getColumnName() + "=\" + ").append(CommonUtils.firstCharToLowerCase(Utililies.tableToEntity(mdc.getColumnName()))).append(" +\", ");
            }
            sb.append("$");
        }
        return sb.toString().replace(", $", "");
    }

    /** 获取主键字段 */
    public DbTableColumn getPrimaryKeyColumn()
    {
        DbTableColumn dc = null;
        if (columnList != null)
        {
            MysqlDbColumn mdc = null;
            for (int i = 0, k = columnList.size(); i < k; i++)
            {
                mdc = (MysqlDbColumn) columnList.get(i);
                if (mdc.isPrimaryKey())
                {
                    dc = mdc;
                    break;
                }
            }
        }
        return dc;
    }

    /** 获取主键字段名 */
    public String getPrimaryColumn()
    {
        if (primaryColumn == null)
        {
            DbTableColumn dc = getPrimaryKeyColumn();
            if (dc != null)
            {
                MysqlDbColumn mdc = (MysqlDbColumn) dc;
                if (mdc.isPrimaryKey())
                {
                    primaryColumn = mdc.getColumnName();
                }
            }
        }
        return primaryColumn;
    }

    public String getPrimaryKey()
    {
        if (primaryKey == null)
        {
            String column = getPrimaryColumn();
            if (column != null)
            {
                primaryKey = Utililies.columnToFeild(column);
            }
        }
        return primaryKey == null ? "" : primaryKey;
    }

    public String getPrimaryJdbcType()
    {
        if (primaryKeyJdbcType == null)
        {
            DbTableColumn dc = getPrimaryKeyColumn();
            if (dc != null)
            {
                MysqlDbColumn mdc = (MysqlDbColumn) dc;
                primaryKeyJdbcType = mdc.getDataType();
            }
        }
        return primaryKeyJdbcType == null ? "varchar" : primaryKeyJdbcType;
    }

    public String getPrimaryJavaType()
    {
        if (primaryKeyJaveType == null)
        {
            DbTableColumn dc = getPrimaryKeyColumn();
            if (dc != null)
            {
                MysqlDbColumn mdc = (MysqlDbColumn) dc;
                primaryKeyJaveType = Utililies.getJavaType(mdc.getDataType());
            }
        }
        return primaryKeyJaveType == null ? "varchar" : primaryKeyJaveType;
    }

    public String getFeildJoin()
    {
        StringBuffer feildJoin = new StringBuffer(Consts.TAB0);
        if (columnList != null)
        {
            MysqlDbColumn mdc = null;
            for (int i = 0, k = columnList.size(); i < k; i++)
            {
                mdc = (MysqlDbColumn) columnList.get(i);
                if (mdc.isPrimaryKey())
                {
                    if (mdc.getExtra() == null || !mdc.getExtra().equalsIgnoreCase("auto_increment"))
                    {
                        feildJoin.append("`" + mdc.getColumnName() + "`").append(", ");
                    }
                }
                else
                {
                    feildJoin.append("`" + mdc.getColumnName() + "`").append(", ");
                }
            }
        }
        return feildJoin.toString().replaceFirst(", $", "");
    }

    public String getAllFeildJoin()
    {
        StringBuffer feildJoin = new StringBuffer(Consts.TAB0);
        if (columnList != null)
        {
            MysqlDbColumn mdc = null;
            for (int i = 0, k = columnList.size(); i < k; i++)
            {
                mdc = (MysqlDbColumn) columnList.get(i);
                feildJoin.append("`" + mdc.getColumnName() + "`").append(" as ").append(Utililies.columnToFeild(mdc.getColumnName())).append(", ");
            }
        }
        return feildJoin.toString().replaceFirst(", $", "");
    }

    public String getFeildMapJoin()
    {
        StringBuffer feildMapJoin = new StringBuffer(Consts.TAB0);
        if (columnList != null)
        {
            if (DBHelper.sections.equals(MySqlSetPanel.sections))
            {
                MysqlDbColumn mdc = null;
                /* #{rowId,jdbcType=BIGINT} */
                String map = "#{{0},jdbcType={1}}";
                for (int i = 0, k = columnList.size(); i < k; i++)
                {
                    mdc = (MysqlDbColumn) columnList.get(i);
                    if (mdc.isPrimaryKey())
                    {
                        if (mdc.getExtra() == null || !mdc.getExtra().equalsIgnoreCase("auto_increment"))
                        {
                            feildMapJoin.append(CommonUtils.format(map, Utililies.columnToFeild(mdc.getColumnName()), Utililies.getJdbcType(mdc.getDataType()))).append(", ");
                        }
                    }
                    else
                    {
                        feildMapJoin.append(CommonUtils.format(map, Utililies.columnToFeild(mdc.getColumnName()), Utililies.getJdbcType(mdc.getDataType()))).append(", ");
                    }
                }
            }
        }
        return feildMapJoin.toString().replaceFirst(", $", "");
    }

    public String getKeyJoin()
    {
        StringBuffer feildMapJoin = new StringBuffer(Consts.TAB0);
        if (columnList != null)
        {
            MysqlDbColumn mdc = null;
            String map = "#{{0},jdbcType={1}}, ";
            int j = 1;
            for (int i = 0, k = columnList.size(); i < k; i++)
            {
                mdc = (MysqlDbColumn) columnList.get(i);
                if (mdc.isPrimaryKey())
                {
                    feildMapJoin.append("WHERE ").append(j > 1 ? " and " : "").append("`" + mdc.getColumnName() + "`" + "=").append(CommonUtils.format(map, Utililies.columnToFeild(mdc.getColumnName()), Utililies.getJdbcType(mdc.getDataType())).replaceFirst(", $", ""));
                    j++;
                }
            }
        }
        return feildMapJoin.toString();
    }

    public String getFeildUp()
    {
        StringBuffer feildSetList = new StringBuffer();
        if (columnList != null)
        {
            MysqlDbColumn mdc = null;
            /* APP_SYS_NAME = #{appSysName,jdbcType=VARCHAR}, */
            String map = "{0} = #{{1},jdbcType={2}}, ";
            for (int i = 0, k = columnList.size(); i < k; i++)
            {
                mdc = (MysqlDbColumn) columnList.get(i);
                if (!mdc.isPrimaryKey())
                {
                    feildSetList.append(Consts.TAB0).append(CommonUtils.format(map, "`" + mdc.getColumnName() + "`", Utililies.columnToFeild(mdc.getColumnName()), Utililies.getJdbcType(mdc.getDataType())));
                }
            }
        }
        return feildSetList.toString().replaceFirst(", $", "");
    }

    public String getKeyModels()
    {
        StringBuffer feildMapJoin = new StringBuffer(Consts.TAB0);
        if (columnList != null)
        {
            MysqlDbColumn mdc = (MysqlDbColumn) getPrimaryKeyColumn();
            if (mdc != null)
            {
                if (mdc.isPrimaryKey())
                {
                    feildMapJoin.append(Utililies.getJavaType(mdc.getDataType()).replaceAll("private ", "")).append(Consts.TAB1).append(Utililies.columnToFeild(mdc.getColumnName()));
                }
            }
        }
        return feildMapJoin.toString();
    }

}
