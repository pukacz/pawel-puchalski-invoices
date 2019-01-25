package pl.coderstrust.invoices.data.file;

import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

class FileHelperTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
//
//    @Mock
//    NumbersProcessor numbersProcessor;
//
//    @Mock
//    FileProcessor fileProcessor;
//
//    @InjectMocks
//    Processor processor;
//
//    @Test
//    public void shouldProcessProvidedInputFileAndSaveResultToProvidedOutputFile() throws IOException {
//        // given
//        String inputFile = "fewLines.txt";
//        when(fileProcessor.readLinesFromFile(Processor.getFilePath(inputFile))).thenReturn(
//            Arrays.asList("1 2 3", "4 5 6", "4 6j", "ads df", "dddd 78"));
//        when(numbersProcessor.processLine("1 2 3")).thenReturn("1+2+3=6");
//        when(numbersProcessor.processLine("4 5 6")).thenReturn("4+5+6=15");
//
//        // when
//        String outputFile = "virtualFewLines.txt";
//        processor.process(Processor.getFilePath(inputFile), Processor.getFilePath(outputFile));
//
//        // then
//        verify(fileProcessor, times(1)).readLinesFromFile(Processor.getFilePath(inputFile));
//        verify(numbersProcessor, times(1)).processLine("4 5 6");
//        verify(numbersProcessor, times(1)).processLine("1 2 3");
//
//        verify(fileProcessor, times(1)).writeLinesToFile(Arrays.asList("1+2+3=6", "4+5+6=15"), Processor.getFilePath(outputFile));
//    }

}