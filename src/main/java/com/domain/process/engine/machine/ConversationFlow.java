package com.domain.process.engine.machine;

import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.ports.outgoing.productinformation.ProductInformationService;
import com.domain.process.engine.machine.controller.InfoController;
import com.domain.process.engine.machine.controller.ProductController;
import com.domain.process.engine.machine.controller.ReportController;
import com.domain.process.engine.machine.controller.SuggestionController;
import com.domain.process.service.BarCodeService;

import java.util.HashMap;

public class ConversationFlow implements MachineFlow {
    private HashMap<Transition, TransitionListener> transitions = new HashMap<>();

    public ConversationFlow(OnNewOutgoingMessageListener listener, ProductInformationService productInformationService, BarCodeService barCodeService) {
        InfoController infoController = new InfoController(listener);
        ProductController productController = new ProductController(listener, barCodeService, productInformationService);
        ReportController reportController = new ReportController(listener, productInformationService);
        SuggestionController suggestionController = new SuggestionController(listener, productInformationService);

        this.addTransition(MachineState.INIT, MachineState.WELCOME, infoController::onWelcome);
        this.addTransition(MachineState.WELCOME, MachineState.WAIT_FOR_ACTION, null);
        this.addTransition(MachineState.WAIT_FOR_ACTION, MachineState.INFO, infoController::onInfo);
        this.addTransition(MachineState.WAIT_FOR_ACTION, MachineState.HELP, infoController::onHelp);
        this.addTransition(MachineState.WAIT_FOR_ACTION, MachineState.METHODOLOGY, infoController::onMethodology);
        this.addTransition(MachineState.WAIT_FOR_ACTION, MachineState.PROCESS_CODE, productController::onText);
        this.addTransition(MachineState.WAIT_FOR_ACTION, MachineState.PROCESS_IMAGE, productController::onImage);
        this.addTransition(MachineState.WAIT_FOR_ACTION, MachineState.INVALID_INPUT_1, infoController::onInvalidInput);
        this.addTransition(MachineState.PROCESS_IMAGE, MachineState.NOT_RECOGNIZED, productController::onNotRecognized);
        this.addTransition(MachineState.PROCESS_IMAGE, MachineState.DISPLAY_RESULTS, productController::onDisplayResults);
        this.addTransition(MachineState.PROCESS_CODE, MachineState.DISPLAY_RESULTS, productController::onDisplayResults);
        this.addTransition(MachineState.PROCESS_IMAGE, MachineState.REPORT_PROMPT_IMAGE, reportController::onReportPromptImage);
        this.addTransition(MachineState.PROCESS_CODE, MachineState.REPORT_PROMPT_IMAGE, reportController::onReportPromptImage);
        this.addTransition(MachineState.DISPLAY_RESULTS, MachineState.ASK_FOR_CHANGES_OR_ACTION, suggestionController::onAskForChangesOrAction);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_1, MachineState.ASK_FOR_TEXT_1, reportController::onAffirmative);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_1, MachineState.WELCOME, infoController::onWelcome);

        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_1, MachineState.PROCESS_IMAGE, productController::onImage);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_1, MachineState.PROCESS_CODE, productController::onText);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_1, MachineState.METHODOLOGY, infoController::onMethodology);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_1, MachineState.INFO, infoController::onInfo);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_1, MachineState.INVALID_INPUT_5, reportController::onInvalidInput1);

        this.addTransition(MachineState.WAIT_FOR_TEXT_1, MachineState.INVALID_INPUT_2, reportController::onInvalidInput2);
        this.addTransition(MachineState.WAIT_FOR_TEXT_1, MachineState.ASK_FOR_ADDITIONAL_IMAGES, reportController::onText);
        this.addTransition(MachineState.WAIT_FOR_DECISION, MachineState.SAVE_REPORT, reportController::onSubmit);
        this.addTransition(MachineState.WAIT_FOR_DECISION, MachineState.INVALID_INPUT_7, reportController::onInvalidInput4);
        this.addTransition(MachineState.WAIT_FOR_DECISION, MachineState.ASK_FOR_IMAGE, reportController::onAdditionalImagesConfirmed);
        this.addTransition(MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION, MachineState.ADD_IMAGE, reportController::onImage);
        this.addTransition(MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION, MachineState.CANCEL_REPORT, reportController::onCancel);
        this.addTransition(MachineState.CANCEL_REPORT, MachineState.WELCOME, infoController::onWelcome);
        this.addTransition(MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION, MachineState.SAVE_REPORT, reportController::onSubmit);
        this.addTransition(MachineState.WAIT_FOR_IMAGE_OR_SUBMISSION, MachineState.INVALID_INPUT_3, reportController::onInvalidInput3);
        this.addTransition(MachineState.SAVE_REPORT, MachineState.WELCOME, infoController::onWelcome);

        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_2, MachineState.ASK_FOR_TEXT_2, suggestionController::onAffirmative);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_2, MachineState.WELCOME, infoController::onWelcome);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_2, MachineState.PROCESS_IMAGE, productController::onImage);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_2, MachineState.PROCESS_CODE, productController::onText);
        this.addTransition(MachineState.WAIT_FOR_TEXT_2, MachineState.INVALID_INPUT_4, suggestionController::onInvalidInput2);
        this.addTransition(MachineState.SAVE_SUGGESTION, MachineState.WELCOME, infoController::onWelcome);

        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_2, MachineState.PROCESS_IMAGE, productController::onImage);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_2, MachineState.PROCESS_CODE, productController::onText);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_2, MachineState.METHODOLOGY, infoController::onMethodology);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_2, MachineState.INFO, infoController::onInfo);
        this.addTransition(MachineState.WAIT_FOR_DECISION_OR_ACTION_2, MachineState.INVALID_INPUT_6, suggestionController::onInvalidInput1);

        this.addTransition(MachineState.WAIT_FOR_TEXT_2, MachineState.SAVE_SUGGESTION, suggestionController::onText);
    }

    @Override
    public void addTransition(MachineState from, MachineState to, TransitionListener listener) {
        transitions.put(new Transition(from, to), listener);
    }

    @Override
    public TransitionListener getTransition(MachineState from, MachineState to) {
        Transition wantedTransition = new Transition(from, to);
        if (transitions.containsKey(wantedTransition)) {
            return transitions.get(new Transition(from, to));
        } else {
            throw new IllegalArgumentException("Transition from " + from  + " to " + to + " not found!");
        }
    }

    @Override
    public boolean containsTransition(MachineState from, MachineState to) {
        return transitions.containsKey(new Transition(from, to));
    }
}
