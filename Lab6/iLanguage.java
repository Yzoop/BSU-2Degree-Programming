package Lab6;


public interface iLanguage {
    enum enLanguage
    {
        eRUS,
        eBEL,
        eENG
    }

    String Get_Translation(String Key);
    void Set_Translation(String KeyField, String theTranslation);
    ///fields
    ///
}
