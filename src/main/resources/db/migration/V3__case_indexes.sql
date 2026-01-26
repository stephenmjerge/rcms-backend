-- Add indexes to speed up common filters and sorting

CREATE INDEX IF NOT EXISTS idx_case_records_status ON case_records(status);
CREATE INDEX IF NOT EXISTS idx_case_records_assigned_to ON case_records(assigned_to);
CREATE INDEX IF NOT EXISTS idx_case_records_created_at ON case_records(created_at);
