(deftemplate user
	(slot id)
	(slot name)
	(multislot purchased_products)
	(multislot products_tags)
)

(deftemplate product
	(slot id)
	(slot name)
	(slot stock)
	(multislot tags)
)

(deftemplate shop_order
	(slot user_id)
	(slot product_id)
)

(deftemplate recommendation
	(slot user_id)
	(slot product_id)
)

(defrule buy
	?sp <- (shop_order (user_id ?uid) (product_id ?pid))
	?u <- (user (id ?uid) (purchased_products $?pp) (products_tags $?upt))
	?p <- (product (id ?pid) (tags $?pt) (stock ?pstock)) =>

	(retract ?sp)
	(modify ?u (purchased_products $?pp ?pid) (products_tags $?upt $?pt))
	(modify ?p (stock (- ?pstock 1)))
)

(defrule invalid_buy
	?sp <- (shop_order) =>

	(retract ?sp)
)

(defrule clear_recommendations_already_purchased
	?u <- (user (id ?uid) (purchased_products $?pp))
	?p <- (product (id ?pid))
	?r <- (recommendation (user_id ?uid) (product_id ?pid))
	(test (member$ ?pid $?pp)) =>

	(retract ?r)
)

(defrule clear_recommendations_out_of_stock
	?p <- (product (id ?pid) (stock 0))
	?r <- (recommendation (product_id ?pid)) =>

	(retract ?r)
)

(defrule out_of_stock
	?p <- (product (stock 0)) =>

	(retract ?p)
)

(defrule update_history_recommendations
	?u <- (user (id ?uid) (purchased_products $?pp) (products_tags $?upt))
	?p <- (product (id ?pid) (tags $?pt))
	(test (and (not (member$ ?pid $?pp)) (subsetp $?pt $?upt))) =>

	(assert (recommendation (user_id ?uid) (product_id ?pid)))
)

(deffacts Data
	(user (id 1) (name Samuel))
	(user (id 2) (name Juan))

	(product (id 1) (name Escoba) (stock 2) (tags limpieza hogar))
	(product (id 2) (name Lejia) (stock 3) (tags limpieza hogar))
	(product (id 3) (name Robot_de_Limpieza) (stock 5) (tags limpieza hogar tecnologia))
	(product (id 4) (name Portatil_de_sobremesa) (stock 4) (tags tecnologia informatica))
	(product (id 5) (name Tarjeta_grafica) (stock 3) (tags tecnologia informatica))
	(product (id 6) (name Yogurtera) (stock 6) (tags comida))
	(product (id 7) (name Pancakes) (stock 7) (tags comida))
	(product (id 8) (name Cafes) (stock 8) (tags comida informatica))
	(product (id 9) (name Teclado_mecanico) (stock 3) (tags tecnologia informatica))

	(shop_order (user_id 1) (product_id 1))
	(shop_order (user_id 1) (product_id 4))

	(shop_order (user_id 2) (product_id 6))
	(shop_order (user_id 2) (product_id 5))
)