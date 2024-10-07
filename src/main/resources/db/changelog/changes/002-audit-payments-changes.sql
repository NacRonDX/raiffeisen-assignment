CREATE TABLE payment_audit (
    payment_id BIGINT,
    operation VARCHAR(255)
);

CREATE TRIGGER payment_insert_audit
AFTER INSERT ON payment
FOR EACH ROW
CALL "com.raiffeisen.processor.repository.trigger.PaymentInsertTrigger";

CREATE TRIGGER payment_delete_audit
AFTER DELETE ON payment
FOR EACH ROW
CALL "com.raiffeisen.processor.repository.trigger.PaymentDeleteTrigger";