ALTER TABLE accounts ADD CONSTRAINT uc_account_agency UNIQUE (agency, account_number);