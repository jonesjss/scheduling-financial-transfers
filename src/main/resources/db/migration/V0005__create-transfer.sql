CREATE TABLE transfers (
  id BIGINT AUTO_INCREMENT NOT NULL,
   user_id BIGINT NOT NULL,
   originating_account_id BIGINT NOT NULL,
   destination_account_id BIGINT NOT NULL,
   creation TIMESTAMP NOT NULL,
   transfer_type VARCHAR(255) NOT NULL,
   scheduling date NOT NULL,
   transfer_value DECIMAL NOT NULL,
   total_value DECIMAL NOT NULL,
   CONSTRAINT pk_transfers PRIMARY KEY (id)
);

ALTER TABLE transfers ADD CONSTRAINT FK_TRANSFERS_ON_DESTINATION_ACCOUNT FOREIGN KEY (destination_account_id) REFERENCES accounts (id);

ALTER TABLE transfers ADD CONSTRAINT FK_TRANSFERS_ON_ORIGINATING_ACCOUNT FOREIGN KEY (originating_account_id) REFERENCES accounts (id);

ALTER TABLE transfers ADD CONSTRAINT FK_TRANSFERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);