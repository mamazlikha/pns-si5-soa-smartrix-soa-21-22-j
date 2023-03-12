package com.smartrix2122soa2122j.smartrix.cucumber.CustomerConsumption;

/*public class CustomerConsumptionStepDef {
    Customer customer;
    List<Measure> measures = new ArrayList<>();
    private final Random random = new Random();
    String current_id = Integer.toHexString(random.nextInt());

    @Mock
    private MeasureRepository measureRepository;

    @Autowired
    private IConsult consult;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
    }
    @Etantdonnéque("la liste de consommation de {string} est vide")
    public void laListeDeConsommationDe(String user_name) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //throw new cucumber.api.PendingException();
        customer=new Customer();
        customer.setId(user_name.replaceAll("\\s+",""));
        customer.setName(user_name);
    }

    @Quand("^je consomme (\\d+) KW le \"([^\"]*)\"$")
    public void jeConsommeKWLe(int consumption, String date) {
        Measure measure=new Measure();
        measure.setCustomerId(customer.getId());
        measure.setEnergyUsed(consumption);
        measure.setId(current_id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate full_date = LocalDate.parse(date, formatter);
        measure.setTimestamp(full_date.atTime(8,5+consumption,9));
        measures.add(measure);
    }

    @Et("la consommation de {string} est demandée")
    public void laConsommationDeEstDemandee(String arg0) {
        this.logger.info("consult memory adress: "+consult);
        when( measureRepository.findByCustomerId(customer.getId()) ).thenReturn(measures);
        consult.setMeasureRepository(measureRepository);
    }

    @Alors("^la consommation de Pierre & Marie pour la journée du \"([^\"]*)\" est de (\\d+) KW$")
    public void laConsommationDelutilisateur(String date, int consumption) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        //throw new cucumber.api.PendingException();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate full_date = LocalDate.parse(date, formatter);
        assertEquals(consumption, consult.getClientConsumption( customer ,full_date.atStartOfDay() , full_date.atTime(23,59,59)).getEnergyUsed() );
    }

    @Et("^la liste des consommation de Pierre & Marie pour la journée du \"([^\"]*)\" est de (\\d+) KW (\\d+) KW et (\\d+) KW$")
    public void laListeDesConsommationDelutilisateur(String date, int e1, int e2, int e3) throws Throwable {
        List<Measure> customer_measures = measureRepository.findByCustomerId(customer.getId());
        for( Measure measure:customer_measures)
            assertTrue(measure.getEnergyUsed()==e1 || measure.getEnergyUsed()==e2 || measure.getEnergyUsed()==e3);
    }

}*/