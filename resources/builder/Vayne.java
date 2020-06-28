package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Vayne extends _Default{
	public Vayne() {
		super("Vayne", "solidor", "solidor");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{3957, 3956};
				break;
			case HP:
				ids = new int[]{3973, 3972};
				break;
			case S1:
				ids = new int[]{3949, 3948};
				break;
			case S2:
				ids = new int[]{3965, 3964};
				break;
			case EX:
				ids = new int[]{7034, 7033};
				break;
			case AA:
				ids = new int[]{7030};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}