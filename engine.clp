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
)