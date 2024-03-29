INSERT INTO public.users(id, first_name, last_name, telegram_id) VALUES (nextval('users_seq'), 'Асель', 'Елемесова', 249395897);
INSERT INTO public.users(id, first_name, last_name, telegram_id) VALUES (nextval('users_seq'), 'Тохтар', 'Елемесов', 73160872);

INSERT INTO public.orders(
    id, address, created_date, customer_name, delivery_date, state, price, quantity, cheesecake_id)
VALUES (nextval('order_seq'), 'Егизбаева 7/5 кв. 10','2019-12-01', 'Айгуль', '2019-12-04', 'COMPLETED', '4700.00', '1.3', 1000);

INSERT INTO public.orders(
    id, address, created_date, customer_name, delivery_date, state, price, quantity, cheesecake_id)
VALUES (nextval('order_seq'), 'Егизбаева 7/9 кв. 33','2019-11-28', 'Тохтар', '2019-12-03', 'COMPLETED', '7000.00', '1.4', 1001);

INSERT INTO public.cheesecakes(
    id, name, price_per_kg)
VALUES (nextval('cheesecake_seq'), 'Фисташковый', '4500');

INSERT INTO public.cheesecakes(
    id, name, price_per_kg)
VALUES (nextval('cheesecake_seq'), 'Орео', '5000');

INSERT INTO public.food(id, name, quantity, unit) VALUES (nextval('food_seq'), 'Бананы', 2, 'KG');
INSERT INTO public.food(id, name, quantity, unit) VALUES (nextval('food_seq'), 'Сливки 33%', 0.5, 'ML');
INSERT INTO public.food(id, name, quantity, unit) VALUES (nextval('food_seq'), 'Фисташки', 200, 'G');
INSERT INTO public.food(id, name, quantity, unit) VALUES (nextval('food_seq'), 'Творожный сыр', 1.5, 'KG');
INSERT INTO public.food(id, name, quantity, unit) VALUES (nextval('food_seq'), 'Масло сливочное', 700, 'G');
INSERT INTO public.food(id, name, quantity, unit) VALUES (nextval('food_seq'), 'Печенье юбилейное', 900, 'G');
INSERT INTO public.food(id, name, quantity, unit) VALUES (nextval('food_seq'), 'Яйца', 1.5, 'KG');






