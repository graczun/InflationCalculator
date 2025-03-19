# Kalkulator Inflacji
Prosta aplikacja pobierająca dane z GUS i obliczająca skumulowaną inflację pomiędzy dwoma okresami.

Przykladowe uzycie:

    public class Main {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.configure(FAIL_ON_NULL_FOR_PRIMITIVES, false);
        MAPPER.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        MAPPER.configure(READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
        MAPPER.configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }
    public static void main(String[] args) {
        try(InflationCalculator calculator = new InflationCalculator(new GusDataProvider(MAPPER))) {
            double inflation = calculator.getInflationRateBetweenDates(YearMonth.of(2023, 12), YearMonth.of(2024, 12));
            System.out.println(inflation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

