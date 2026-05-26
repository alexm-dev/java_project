-- ShareSpace reference data
-- Still needs to be discussed to what reference data we want.
-- Admin panel might be used to add reference data if we want as well.

INSERT INTO categories (name, description)
SELECT 'Electronics', 'Phones, TVs and other electronics'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Electronics');

INSERT INTO categories (name, description)
SELECT 'Tools', 'Power tools and hand tools'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Tools');

INSERT INTO sub_categories (name, category_id)
SELECT 'Smartphone', (SELECT id FROM categories WHERE name = 'Electronics')
WHERE NOT EXISTS (
    SELECT 1 FROM sub_categories
    WHERE name = 'Smartphone'
      AND category_id = (SELECT id FROM categories WHERE name = 'Electronics')
);

INSERT INTO sub_categories (name, category_id)
SELECT 'TV', (SELECT id FROM categories WHERE name = 'Electronics')
WHERE NOT EXISTS (
    SELECT 1 FROM sub_categories
    WHERE name = 'TV'
      AND category_id = (SELECT id FROM categories WHERE name = 'Electronics')
);
