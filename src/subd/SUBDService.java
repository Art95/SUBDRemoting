package subd;

/**
 * Created by Artem on 21.10.2016.
 */
import database.DataBase;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@WebService
public class SUBDService
{
    private Map<String, DataBase> dataBaseMap;
    private final String folderAddress = "./Databases/";

    @WebMethod
    public void createDataBase(@WebParam(name="name") String name)
    {
        if (dataBaseMap == null)
            loadDataBases();

        if (dataBaseMap.containsKey(name)) {
            System.out.println("Data base " + name + " already exists!");
            return;
        }

        DataBase db = new DataBase(name);
        dataBaseMap.put(name, db);
    }

    @WebMethod
    public void dropDataBase(@WebParam(name="name") String name) {
        if (dataBaseMap == null)
            loadDataBases();

        if (dataBaseMap.containsKey(name)) {
            dataBaseMap.remove(name);

            File file = new File(folderAddress + name + ".txt");
            file.delete();
        }
    }

    @WebMethod
    public void saveDataBase(@WebParam(name="name") String name, @WebParam(name="data") String data) {
        if (dataBaseMap == null)
            loadDataBases();

        try {
            if (dataBaseMap.containsKey(name)) {

                dataBaseMap.put(name, DataBase.parseJson(data));
                dataBaseMap.get(name).saveDataBase(folderAddress + name + ".txt");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @WebMethod
    public String getDataBaseData(@WebParam(name="name") String name) {
        if (dataBaseMap == null)
            loadDataBases();

        if (dataBaseMap.containsKey(name))
            return dataBaseMap.get(name).toJson();

        return null;
    }

    @WebMethod
    public String[] getDataBasesNames() {
        if (dataBaseMap == null)
            loadDataBases();

        Object[] names = dataBaseMap.keySet().toArray();

        String[] res = new String[names.length];

        for (int i = 0; i < names.length; ++i)
            res[i] = names[i].toString();

        return res;
    }

    private void loadDataBases() {
        dataBaseMap = new HashMap<>();

        File dir = new File(folderAddress);
        File[] files = dir.listFiles();

        for (File file : files) {
            try {
                DataBase db = DataBase.loadDataBase(file.getAbsolutePath());
                dataBaseMap.put(db.getName(), db);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}