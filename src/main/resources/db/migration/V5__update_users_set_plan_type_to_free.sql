UPDATE users
SET plan_type = 'FREE'
WHERE plan_type IS NULL;
