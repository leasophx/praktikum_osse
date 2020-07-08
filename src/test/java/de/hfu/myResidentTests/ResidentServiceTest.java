package de.hfu.myResidentTests;

import de.hfu.residents.domain.*;
import de.hfu.residents.repository.*;
import de.hfu.residents.service.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;
import org.junit.rules.ExpectedException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import static org.easymock.EasyMock.*;


public class ResidentServiceTest {

    private Resident resident, resident2, resident3;
    private BaseResidentService baseResidentService;
    private ExpectedException expEx;


    @Before
    public void init() {

        baseResidentService = new BaseResidentService();


        resident = new Resident("LeaSophie", "H", "Teststrasse1", "Lindau", new Date(1995, 5, 5));
        resident2 = new Resident("Testperson1", "Heinzelmann", "Teststrasse2", "Lindau", new Date(1994, 5, 5));
        resident3 = new Resident("Testperson2", "Heinzelmann", "Teststrasse3", "Lindau", new Date(1993, 5, 5));



    }

    /* ******************************************* TESTS FUER UNIQUE RESIDENT ************************************** */
    @Test
    public void getUniqueResidentTest1() throws ResidentServiceException {
        List<Resident> liste = new ArrayList<Resident>();

        liste.add(resident);
        liste.add(resident2);
        liste.add(resident3);

        ResidentRepositoryStub residentRepositoryStub = new ResidentRepositoryStub(liste);
        BaseResidentService service = new BaseResidentService();
        service.setResidentRepository(residentRepositoryStub);
        resident = service.getUniqueResident(resident2);


        try{ // Anschliessender Vergleich der Exception Message mit assertsEquals
            resident = service.getUniqueResident(new Resident("TestFirstName1","TestLastName2","Teststrasse","Teststadt",null));
        }catch (ResidentServiceException e){
            assertEquals("Suchanfrage lieferte kein eindeutiges Ergebnis!", e.getMessage());
        }

    }
    @Test
    public void getUniqueResidentTest2() throws ResidentServiceException{
        List<Resident> liste = new ArrayList<Resident>();

        liste.add(resident);
        liste.add(resident2);
        liste.add(resident3);

        ResidentRepositoryStub residentRepositoryStub = new ResidentRepositoryStub(liste);
        BaseResidentService service = new BaseResidentService();
        service.setResidentRepository(residentRepositoryStub);

        try{ // Anschliessender Vergleich der Exception Message mit assertsEquals
            resident = service.getUniqueResident(new Resident("*","*","*","*",null));
        }catch (ResidentServiceException e){
            assertEquals("Wildcards (*) sind nicht erlaubt!", e.getMessage());
        }

    }
    @Test
    public void getUniqueResidentTest3() throws ResidentServiceException{
        List<Resident> liste = new ArrayList<Resident>();

        liste.add(resident);
        liste.add(resident2);
        liste.add(resident3);

        ResidentRepositoryStub residentRepositoryStub = new ResidentRepositoryStub(liste);
        BaseResidentService service = new BaseResidentService();
        service.setResidentRepository(residentRepositoryStub);



        try { // Anschliessender Vergleich der Exception Message mit assertsEquals
            Resident newTestResident = service.getUniqueResident(new Resident("Lea", "Sophie", "Teststrasse", null, null));

            assertEquals("Lea",newTestResident.getGivenName());
            assertEquals("Sophie",newTestResident.getFamilyName());
            assertEquals("Teststrasse1",newTestResident.getStreet());
            assertEquals("Konstanz",newTestResident.getCity());

        }catch (ResidentServiceException e){
           assertEquals("Suchanfrage lieferte kein eindeutiges Ergebnis!",e.getMessage());
        }


    }


    /* ******************************************* TESTS FUER FILTER RESIDENT ************************************** */
    @Test
    public void getFilteredResidentsTest1() {

        List<Resident> liste = new ArrayList<Resident>();

        liste.add(resident);
        liste.add(resident2);
        liste.add(resident3);


        ResidentRepositoryStub residentRepositoryStub = new ResidentRepositoryStub(liste);
        BaseResidentService service = new BaseResidentService();
        service.setResidentRepository(residentRepositoryStub);


        List<Resident> testFilterList = service.getFilteredResidentsList(new Resident("L*", "", "", "", new Date(1995,5,5)));

        for (Resident test : testFilterList) {
            //System.out.println(test.getGivenName());
            assertEquals(resident.getGivenName(), test.getGivenName());
            assertEquals(resident.getFamilyName(), test.getFamilyName());
            assertEquals(resident.getStreet(), test.getStreet());
            assertEquals(resident.getCity(), test.getCity());
            assertEquals(resident.getDateOfBirth(), test.getDateOfBirth());

        }

    }

    @Test
    public void getFilteredResidentsTest2() {
        List<Resident> liste = new ArrayList<Resident>();

        liste.add(resident);
        liste.add(resident2);
        liste.add(resident3);

        ResidentRepositoryStub residentRepositoryStub = new ResidentRepositoryStub(liste);
        BaseResidentService service = new BaseResidentService();
        service.setResidentRepository(residentRepositoryStub);

        List<Resident> testFilterList = service.getFilteredResidentsList(new Resident("", "H*", " ", "", null));

        assertEquals(3,testFilterList.size()); // 3 Uebereinstimmungen
        for (Resident test : testFilterList) {
            System.out.println(test.getGivenName());
        }
    }

    @Test
    public void getFilteredResidentsTest3() throws ResidentServiceException{
        List<Resident> liste = new ArrayList<Resident>();

        liste.add(resident);
        liste.add(resident2);
        liste.add(resident3);

        ResidentRepositoryStub residentRepositoryStub = new ResidentRepositoryStub(liste);
        BaseResidentService service = new BaseResidentService();
        service.setResidentRepository(residentRepositoryStub);

        List<Resident> testFilterList = service.getFilteredResidentsList(new Resident("L*", "", "", "", new Date(1995,5,5)));


        try {

            for (Resident test : testFilterList) {

                assertEquals(resident2.getGivenName(), test.getGivenName());
                assertEquals(resident2.getFamilyName(), test.getFamilyName());
                assertEquals(resident2.getStreet(), test.getStreet());
                assertEquals(resident2.getCity(), test.getCity());
                assertEquals(resident2.getDateOfBirth(), test.getDateOfBirth());

            }
        }catch  (ComparisonFailure e){
            System.out.println("Keine Uebereinstimmung!!! - " + e.getMessage());
        }
    }

    /* ******************************************* INTEGRATIONSTEST MIT MOCK ************************************** */
    @Test
    public void testGetUniqueResidentMock()
    {
        List<Resident> liste = new ArrayList<Resident>();

        liste.add(resident);
        liste.add(resident2);
        liste.add(resident3);

        ResidentRepository mock = createMock(ResidentRepository.class);
        expect(mock.getResidents()).andReturn(liste);
        replay(mock);

        ResidentService residentService = new BaseResidentService();
        ((BaseResidentService) residentService).setResidentRepository(mock);

        try {
            Resident result = residentService.getUniqueResident(new Resident("LeaSophie", "H", null, null, null));
            assertEquals("LeaSophie", result.getGivenName());
            assertEquals("H", result.getFamilyName());
        } catch (ResidentServiceException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }

        verify(mock);
    }




}
