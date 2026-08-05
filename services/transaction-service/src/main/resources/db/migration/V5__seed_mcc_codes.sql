INSERT INTO mcc_category_mapping (mcc, category_id)
  SELECT mcc, c.id
  FROM (VALUES
      ('5411', 'Lebensmittel'),
      ('5422', 'Lebensmittel'),
      ('5451', 'Lebensmittel'),
      ('5462', 'Lebensmittel'),

      ('5541', 'Transport'),
      ('5542', 'Transport'),
      ('4111', 'Transport'),
      ('4121', 'Transport'),

      ('5912', 'Gesundheit'),
      ('8011', 'Gesundheit'),
      ('8021', 'Gesundheit'),
      ('8062', 'Gesundheit'),

      ('5813', 'Unterhaltung'),
      ('7832', 'Unterhaltung'),
      ('7922', 'Unterhaltung'),
      ('7996', 'Unterhaltung'),

      ('4900', 'Wohnen')
  ) AS m(mcc, category_name)
  JOIN category c
    ON c.name = m.category_name
   AND c.is_system = TRUE;