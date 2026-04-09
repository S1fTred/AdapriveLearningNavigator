-- Manual migration for existing local H2 database when spring.sql.init.mode=never
-- Run this once in H2 Console before using /auth/register, /auth/login or /auth/refresh.

ALTER TABLE users ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255);
