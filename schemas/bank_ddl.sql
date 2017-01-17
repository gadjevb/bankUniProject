create table accounts(
account_ID         INT            NOT NULL PRIMARY KEY AUTO_INCREMENT,
account_Name       VARCHAR(50)    NOT NULL,
account_Password   VARCHAR(18)    NOT NULL,
account_Balance    DOUBLE         NOT NULL
);

create table sessions(
session_ID                VARCHAR(100)    NOT NULL PRIMARY KEY,
session_Name              VARCHAR(50)     NOT NULL,
session_CreationDate      DATETIME        NOT NULL
);

CREATE TABLE transaction_history(
transaction_Date        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
account_Name            VARCHAR(50)      NOT NULL,
transaction_Operation   VARCHAR(10)      NOT NULL,
transaction_Amount      DOUBLE           NOT NULL
);

CREATE TABLE transfer_history(
transfer_Date        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
account_NameFrom     VARCHAR(50)      NOT NULL,
account_NameTo       VARCHAR(50)      NOT NULL,
transfer_Amount      DOUBLE           NOT NULL
);

DELIMITER $$
CREATE TRIGGER transaction_history BEFORE UPDATE ON accounts
FOR EACH ROW
BEGIN
IF (NEW.account_Balance > OLD.account_Balance) THEN
            SET @transaction_Operation = "Deposit";
            SET @OpValue = NEW.account_Balance - OLD.account_Balance;
      ELSE
            SET @transaction_Operation = "Withdraw";
            SET @OpValue = OLD.account_Balance - NEW.account_Balance;
      END IF;
INSERT INTO transaction_history VALUES(NULL , OLD.account_Name, @transaction_Operation, @OpValue);
end$$
DELIMITER ;
