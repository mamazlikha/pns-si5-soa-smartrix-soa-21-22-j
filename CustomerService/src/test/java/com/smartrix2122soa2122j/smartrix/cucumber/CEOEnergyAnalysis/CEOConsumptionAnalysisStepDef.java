package com.smartrix2122soa2122j.smartrix.cucumber.CEOEnergyAnalysis;

/*@SpringBootTest
@ContextConfiguration(classes = SmartrixApplication.class)
public class CEOConsumptionAnalysisStepDef {

    @Mock
    private MeasureRepository measureRepository; // DB mocked

    @Mock
    private CustomerRepository customerRepository;

    @Autowired
    private CounterWS counter;

    @Autowired
    private ProviderCareWS providerCareWS;

    @Autowired
    private IConsult consult;

    private Customer frank;

    private Customer laurent;

    private String totalEnergyConsumed;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Etantdonnéque("une énergie de {int}KW fournit à smartrix grid par la société de Nicolas pour le mois de {string} {int}")
    public void setup(int energy, String month, int year) {
        frank = new Customer();
        frank.setId("bb1Afx");
        frank.setName("Frank");

        laurent = new Customer();
        laurent.setName("Laurent");
        laurent.setId("21s3Fv");

        // -- Franck consumptions --
        var measureFrank1 = new Measure();
        measureFrank1.setCustomerId(frank.getId());
        measureFrank1.setEnergyUsed(137);
        measureFrank1.setId("erz13sl");
        measureFrank1.setTimestamp(LocalDateTime.of(2021, 1, 11, 0, 0, 0));

        var measureFrank2 = new Measure();
        measureFrank2.setCustomerId(frank.getId());
        measureFrank2.setEnergyUsed(150);
        measureFrank2.setId("qd2sgX9");
        measureFrank2.setTimestamp(LocalDateTime.of(2021, 1, 18, 0, 0, 0));

        var measureFrank3 = new Measure();
        measureFrank3.setCustomerId(frank.getId());
        measureFrank3.setEnergyUsed(180);
        measureFrank3.setId("Htf39j0");
        measureFrank3.setTimestamp(LocalDateTime.of(2021, 1, 28, 0, 0, 0));


        // -- Laurent consumptions --
        var measureLaurent1 = new Measure();
        measureLaurent1.setCustomerId(laurent.getId());
        measureLaurent1.setEnergyUsed(90);
        measureLaurent1.setId("t5fh3v");
        measureLaurent1.setTimestamp(LocalDateTime.of(2021, 1, 22, 0, 0, 0));

        var measureLaurent2 = new Measure();
        measureLaurent2.setCustomerId(laurent.getId());
        measureLaurent2.setEnergyUsed(100);
        measureLaurent2.setId("c4t50ng");
        measureLaurent2.setTimestamp(LocalDateTime.of(2021, 1, 23, 0, 0, 0));

        var measureLaurent3 = new Measure();
        measureLaurent3.setCustomerId(laurent.getId());
        measureLaurent3.setEnergyUsed(80);
        measureLaurent3.setId("32fgqs4");
        measureLaurent3.setTimestamp(LocalDateTime.of(2021, 1, 24, 0, 0, 0));

        List<Measure> frankMeasures = new ArrayList<>();
        List<Measure> laurentMeasures = new ArrayList<>();

        frankMeasures.add(measureFrank1);
        frankMeasures.add(measureFrank2);
        frankMeasures.add(measureFrank3);

        laurentMeasures.add(measureLaurent1);
        laurentMeasures.add(measureLaurent2);
        laurentMeasures.add(measureLaurent3);

        List<Measure> allMeasures = new ArrayList<>();

        allMeasures.add(measureFrank1);
        allMeasures.add(measureFrank2);
        allMeasures.add(measureFrank3);
        allMeasures.add(measureLaurent1);
        allMeasures.add(measureLaurent2);
        allMeasures.add(measureLaurent3);

        customerRepository.save(frank);
        customerRepository.save(laurent);

        counter.save(measureFrank1);
        counter.save(measureFrank2);
        counter.save(measureFrank3);

        counter.save(measureLaurent1);
        counter.save(measureLaurent2);
        counter.save(measureLaurent3);

        consult.setMeasureRepository(measureRepository);

        when (measureRepository.findAll()).thenReturn(allMeasures);

        when(measureRepository.findByCustomerId(frank.getId())).thenReturn(frankMeasures);

        when(measureRepository.findByCustomerId(laurent.getId())).thenReturn(laurentMeasures);
    }

    @Quand("Je demande le total de l'énergie consommée sur la mois de {string}")
    public void consultAttempt(String month) {
        totalEnergyConsumed = providerCareWS.totalConsumed();
    }

    @Alors("l'énergie consommée provenant de mon entreprise est de {int} KW")
    public void assertion1(int arg0) {
        assertEquals(String.valueOf(arg0), totalEnergyConsumed);
    }

    @Et("Franck a consommé {int}")
    public void assertion2(int arg0) {

        LocalDateTime start = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0);
        assertEquals(String.valueOf(arg0), providerCareWS.getCustomerConsumption(frank, start, end));
    }

    @Et("Laurent a consommé {int}")
    public void assertion3(int arg0) {

        LocalDateTime start = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0);
        assertEquals(String.valueOf(arg0), providerCareWS.getCustomerConsumption(laurent, start, end));
    }


}*/
