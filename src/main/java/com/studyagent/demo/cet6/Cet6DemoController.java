package com.studyagent.demo.cet6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/demo/cet6")
public class Cet6DemoController {

    private final Cet6DemoService cet6DemoService;

    public Cet6DemoController(
            Cet6DemoService cet6DemoService
    ) {
        this.cet6DemoService = cet6DemoService;
    }

    @GetMapping("/preview")
    public List<Cet6PreviewResponse> preview()
            throws IOException {

        return cet6DemoService.previewReadingMistakes();
    }

    @GetMapping("/diagnose-first")
    public Cet6DiagnosisResponse diagnoseFirst()
            throws IOException {

        return cet6DemoService
                .diagnoseFirstReadingMistake();
    }

    @GetMapping("/diagnose-three")
    public List<Cet6DiagnosisResponse> diagnoseThree()
            throws IOException {

        return cet6DemoService
                .diagnoseFirstThreeReadingMistakes();
    }

    @GetMapping("/profile-preview")
    public Cet6MiniProfileResponse profilePreview()
            throws IOException {

        return cet6DemoService.buildMiniProfile();
    }

    @GetMapping("/diagnose/{questionNo}")
    public Cet6DiagnosisResponse diagnoseByQuestionNo(
            @PathVariable Integer questionNo
    ) throws IOException {

        return cet6DemoService
                .diagnoseReadingQuestion(
                        questionNo
                );
    }

    @GetMapping("/reading-report")
    public Cet6ReadingReportResponse readingReport()
            throws IOException {

        return cet6DemoService
                .buildReadingReport();
    }

    @GetMapping("/reading-diagnosis")
    public List<Cet6SectionDiagnosisResponse>
    readingDiagnosis() throws IOException {

        return cet6DemoService
                .diagnoseReadingSections();
    }

    @GetMapping("/reading-profile")
    public Cet6ReadingProfileResponse
    readingProfile() throws IOException {

        return cet6DemoService
                .buildReadingProfile();
    }

    @GetMapping("/demo-listening-report")
    public Cet6ObjectiveSectionReport
    demoListeningReport() throws IOException {

        return cet6DemoService
                .buildDemoListeningReport();
    }

    @GetMapping("/demo-writing-evaluation")
    public Cet6SubjectiveEvaluationResponse
    demoWritingEvaluation() throws IOException {

        return cet6DemoService
                .evaluateDemoWriting();
    }

    @GetMapping("/demo-translation-evaluation")
    public Cet6SubjectiveEvaluationResponse
    demoTranslationEvaluation() throws IOException {

        return cet6DemoService
                .evaluateDemoTranslation();
    }

    @GetMapping("/report")
    public Cet6DemoReportResponse report()
            throws IOException {

        return cet6DemoService
                .buildFinalDemoReport();
    }
}