package com.cbecs.generator.main;

import java.awt.Toolkit;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.cbecs.generator.db.DBHelper;
import com.cbecs.generator.db.DbCache;
import com.cbecs.generator.entity.CreAttr;
import com.cbecs.generator.entity.DbTableColumn;
import com.cbecs.generator.form.MainWindow;
import com.cbecs.generator.util.Utililies;

public class CreateThread extends Thread
{
    List<String> tables = null;
    CreAttr ca = null;

    public CreateThread(List<String> tables, CreAttr ca)
    {
        this.tables = tables;
        this.ca = ca;
    }

    public void run()
    {
        MainWindow.mainWindow.showProcessPanel();
        Map<String, List<DbTableColumn>> tabelMap = getTableColumn();

        if (tabelMap != null)
        {
            DbCache.mysqlDbColumnMap = tabelMap;
        }

        if (tables.size() == 0)
        {
            tables.add(Utililies.entityToTable(ca.getEntityName()));
        }
        List<DbTableColumn> columnList = null;
        String tableName = null;
        CreAttr creAtt = null;
        for (int i = 0, k = tables.size(); i < k; i++)
        {
            tableName = tables.get(i);
            columnList = DbCache.mysqlDbColumnMap.get(tableName);
            creAtt = ca.clone();
            new Creater(creAtt, tableName, columnList).run();
        }
        // 执行完成
        MainWindow.mainWindow.hideProcessPanel();
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, "恭喜，生成完成\n路径：" + ca.getSaveDir(), "提示", JOptionPane.ERROR_MESSAGE);

    }

    public Map<String, List<DbTableColumn>> getTableColumn()
    {
        DBHelper.initDbConfig();
        return DBHelper.getMutilTableColumns(this.tables, DBHelper.schema);
    }
}
